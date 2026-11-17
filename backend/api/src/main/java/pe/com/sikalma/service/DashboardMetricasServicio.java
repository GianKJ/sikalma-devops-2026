package pe.com.sikalma.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.sikalma.dto.ResumenMetricasDto;
import pe.com.sikalma.dto.SerieGraficoDto;
import pe.com.sikalma.entity.Cita;
import pe.com.sikalma.entity.EstadoCita;
import pe.com.sikalma.entity.ModalidadAtencion;
import pe.com.sikalma.entity.Paciente;
import pe.com.sikalma.repository.CitaRepositorio;
import pe.com.sikalma.repository.EspecialidadRepositorio;
import pe.com.sikalma.repository.PacienteRepositorio;
import pe.com.sikalma.repository.PsicologoRepositorio;
import pe.com.sikalma.repository.ServicioRepositorio;
import pe.com.sikalma.repository.UsuarioSistemaRepositorio;

@Service
public class DashboardMetricasServicio {

    private static final List<String> PALETA_SIKALMA = List.of(
            "#315f58", "#d68651", "#57402e", "#fac8a9", "#75685e", "#2d6a4f");

    private final PacienteRepositorio pacienteRepositorio;
    private final PsicologoRepositorio psicologoRepositorio;
    private final ServicioRepositorio servicioRepositorio;
    private final EspecialidadRepositorio especialidadRepositorio;
    private final UsuarioSistemaRepositorio usuarioRepositorio;
    private final CitaRepositorio citaRepositorio;
    private final Clock reloj;

    public DashboardMetricasServicio(
            PacienteRepositorio pacienteRepositorio,
            PsicologoRepositorio psicologoRepositorio,
            ServicioRepositorio servicioRepositorio,
            EspecialidadRepositorio especialidadRepositorio,
            UsuarioSistemaRepositorio usuarioRepositorio,
            CitaRepositorio citaRepositorio,
            Clock reloj) {
        this.pacienteRepositorio = pacienteRepositorio;
        this.psicologoRepositorio = psicologoRepositorio;
        this.servicioRepositorio = servicioRepositorio;
        this.especialidadRepositorio = especialidadRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.citaRepositorio = citaRepositorio;
        this.reloj = reloj;
    }

    @Transactional(readOnly = true)
    public ResumenMetricasDto calcularMetricas() {
        List<Paciente> pacientes = pacienteRepositorio.findAllByOrderByApellidosAscNombresAsc();
        List<Cita> citas = citaRepositorio.findAllByOrderByFechaDescHoraInicioAsc();
        LocalDate hoy = LocalDate.now(reloj);

        long pacientesActivos = pacientes.stream().filter(Paciente::isActivo).count();
        long conConsentimiento = pacientes.stream().filter(Paciente::isConsentimientoRegistrado).count();

        long citasHoy = citas.stream().filter(c -> hoy.equals(c.getFecha())).count();
        long pendientes = contarPorEstado(citas, EstadoCita.PENDIENTE);
        long confirmadas = contarPorEstado(citas, EstadoCita.CONFIRMADA);
        long enSala = contarPorEstado(citas, EstadoCita.EN_SALA);
        long atendidas = contarPorEstado(citas, EstadoCita.ATENDIDA);
        long canceladas = contarPorEstado(citas, EstadoCita.CANCELADA);

        int tasaAsistencia = calcularTasaAsistencia(citas.size(), canceladas);

        long presenciales = citas.stream().filter(c -> c.getModalidad() == ModalidadAtencion.PRESENCIAL).count();
        long virtuales = citas.stream().filter(c -> c.getModalidad() == ModalidadAtencion.VIRTUAL).count();

        List<SerieGraficoDto> porEstado = List.of(
                new SerieGraficoDto("Pendientes", pendientes, "#d68651"),
                new SerieGraficoDto("Confirmadas", confirmadas, "#315f58"),
                new SerieGraficoDto("En sala", enSala, "#2d6a4f"),
                new SerieGraficoDto("Atendidas", atendidas, "#57402e"),
                new SerieGraficoDto("Canceladas", canceladas, "#a53d3d"));

        List<SerieGraficoDto> porServicio = construirSeriePorServicio(citas);

        List<SerieGraficoDto> porModalidad = List.of(
                new SerieGraficoDto("Presencial", presenciales, "#315f58"),
                new SerieGraficoDto("Virtual", virtuales, "#d68651"));

        List<Cita> proximas = citas.stream()
                .filter(c -> c.getEstado() != EstadoCita.CANCELADA)
                .filter(c -> !c.getFecha().isBefore(hoy))
                .sorted(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHoraInicio))
                .limit(6)
                .toList();

        return new ResumenMetricasDto(
                pacientes.size(),
                pacientesActivos,
                conConsentimiento,
                psicologoRepositorio.count(),
                servicioRepositorio.count(),
                especialidadRepositorio.count(),
                usuarioRepositorio.countByActivoTrue(),
                citas.size(),
                citasHoy,
                pendientes,
                confirmadas,
                enSala,
                atendidas,
                canceladas,
                tasaAsistencia,
                presenciales,
                virtuales,
                porEstado,
                porServicio,
                porModalidad,
                proximas);
    }

    private int calcularTasaAsistencia(int totalCitas, long canceladas) {
        if (totalCitas <= 0 || canceladas >= totalCitas) {
            return 0;
        }
        return (int) Math.round(((double) (totalCitas - canceladas) * 100.0) / totalCitas);
    }

    private long contarPorEstado(List<Cita> citas, EstadoCita estado) {
        return citas.stream().filter(c -> c.getEstado() == estado).count();
    }

    private List<SerieGraficoDto> construirSeriePorServicio(List<Cita> citas) {
        Map<String, Long> conteo = new LinkedHashMap<>();
        for (Cita cita : citas) {
            String nombre = cita.getServicio().getNombre();
            conteo.merge(nombre, 1L, Long::sum);
        }
        List<SerieGraficoDto> resultado = new ArrayList<>();
        int indice = 0;
        for (Map.Entry<String, Long> entry : conteo.entrySet()) {
            String color = PALETA_SIKALMA.get(indice % PALETA_SIKALMA.size());
            resultado.add(new SerieGraficoDto(entry.getKey(), entry.getValue(), color));
            indice++;
        }
        return resultado;
    }
}
