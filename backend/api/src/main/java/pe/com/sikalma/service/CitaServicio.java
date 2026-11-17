package pe.com.sikalma.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.sikalma.dto.CitaFormulario;
import pe.com.sikalma.dto.NotaEvolucionFormulario;
import pe.com.sikalma.entity.Cita;
import pe.com.sikalma.entity.EstadoCita;
import pe.com.sikalma.entity.Paciente;
import pe.com.sikalma.entity.Psicologo;
import pe.com.sikalma.entity.Servicio;
import pe.com.sikalma.exception.ConflictoHorarioException;
import pe.com.sikalma.exception.RegistroNoEncontradoException;
import pe.com.sikalma.repository.CitaRepositorio;
import pe.com.sikalma.repository.PacienteRepositorio;
import pe.com.sikalma.repository.PsicologoRepositorio;
import pe.com.sikalma.repository.ServicioRepositorio;

@Service
public class CitaServicio {

    private static final LocalTime HORA_APERTURA = LocalTime.of(9, 0);
    private static final LocalTime HORA_CIERRE = LocalTime.of(19, 30);

    private final CitaRepositorio repositorio;
    private final PacienteRepositorio pacienteRepositorio;
    private final PsicologoRepositorio psicologoRepositorio;
    private final ServicioRepositorio servicioRepositorio;

    public CitaServicio(
            CitaRepositorio repositorio,
            PacienteRepositorio pacienteRepositorio,
            PsicologoRepositorio psicologoRepositorio,
            ServicioRepositorio servicioRepositorio) {
        this.repositorio = repositorio;
        this.pacienteRepositorio = pacienteRepositorio;
        this.psicologoRepositorio = psicologoRepositorio;
        this.servicioRepositorio = servicioRepositorio;
    }

    @Transactional(readOnly = true)
    public List<Cita> listar(String buscar, EstadoCita estado, Long psicologoId) {
        String filtro = TextoNormalizadorUtil.opcionalMinuscula(buscar);
        return repositorio.findAllByOrderByFechaDescHoraInicioAsc().stream()
                .filter(cita -> estado == null || cita.getEstado() == estado)
                .filter(cita -> psicologoId == null || Objects.equals(cita.getPsicologo().getId(), psicologoId))
                .filter(cita -> filtro == null || coincideBusqueda(cita, filtro))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Cita> listarParaProfesional(Long psicologoId, EstadoCita estado) {
        List<Cita> base = psicologoId == null
                ? repositorio.findAllByOrderByFechaDescHoraInicioAsc()
                : repositorio.findByPsicologoIdOrderByFechaDescHoraInicioAsc(psicologoId);
        return base.stream()
                .filter(cita -> estado == null || cita.getEstado() == estado)
                .toList();
    }

    @Transactional(readOnly = true)
    public Cita obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RegistroNoEncontradoException("La cita solicitada no existe."));
    }

    @Transactional(readOnly = true)
    public CitaFormulario obtenerFormulario(Long id) {
        Cita cita = obtenerPorId(id);
        CitaFormulario formulario = new CitaFormulario();
        formulario.setId(cita.getId());
        formulario.setPacienteId(cita.getPaciente().getId());
        formulario.setPsicologoId(cita.getPsicologo().getId());
        formulario.setServicioId(cita.getServicio().getId());
        formulario.setFecha(cita.getFecha());
        formulario.setHoraInicio(cita.getHoraInicio());
        formulario.setModalidad(cita.getModalidad());
        formulario.setEstado(cita.getEstado());
        formulario.setMotivoConsulta(cita.getMotivoConsulta());
        formulario.setNotaEvolucion(cita.getNotaEvolucion());
        formulario.setDiagnosticoReferencial(cita.getDiagnosticoReferencial());
        return formulario;
    }

    @Transactional(readOnly = true)
    public NotaEvolucionFormulario obtenerFormularioNota(Long citaId) {
        Cita cita = obtenerPorId(citaId);
        NotaEvolucionFormulario formulario = new NotaEvolucionFormulario();
        formulario.setCitaId(cita.getId());
        formulario.setNotaEvolucion(cita.getNotaEvolucion());
        formulario.setDiagnosticoReferencial(cita.getDiagnosticoReferencial());
        formulario.setEstado(EstadoCita.ATENDIDA);
        return formulario;
    }

    @Transactional
    public Cita guardar(CitaFormulario formulario) {
        Paciente paciente = pacienteRepositorio.findById(formulario.getPacienteId())
                .orElseThrow(() -> new RegistroNoEncontradoException("El paciente seleccionado no existe."));
        Psicologo psicologo = psicologoRepositorio.findById(formulario.getPsicologoId())
                .orElseThrow(() -> new RegistroNoEncontradoException("El profesional seleccionado no existe."));
        Servicio servicio = servicioRepositorio.findById(formulario.getServicioId())
                .orElseThrow(() -> new RegistroNoEncontradoException("El servicio seleccionado no existe."));

        int duracion = servicio.getDuracionMinutos();
        if (formulario.getEstado() != EstadoCita.CANCELADA) {
            validarDisponibilidad(
                    psicologo.getId(),
                    formulario.getFecha(),
                    formulario.getHoraInicio(),
                    duracion,
                    formulario.getId());
        }

        Cita entidad = formulario.getId() == null ? new Cita() : obtenerPorId(formulario.getId());
        entidad.setPaciente(paciente);
        entidad.setPsicologo(psicologo);
        entidad.setServicio(servicio);
        entidad.setFecha(formulario.getFecha());
        entidad.setHoraInicio(formulario.getHoraInicio());
        entidad.setDuracionMinutos(duracion);
        entidad.setModalidad(formulario.getModalidad());
        entidad.setEstado(formulario.getEstado());
        entidad.setMotivoConsulta(TextoNormalizadorUtil.opcional(formulario.getMotivoConsulta()));
        entidad.setNotaEvolucion(TextoNormalizadorUtil.opcional(formulario.getNotaEvolucion()));
        entidad.setDiagnosticoReferencial(TextoNormalizadorUtil.opcional(formulario.getDiagnosticoReferencial()));
        return repositorio.save(entidad);
    }

    @Transactional
    public void cambiarEstado(Long id, EstadoCita nuevoEstado) {
        Cita entidad = obtenerPorId(id);
        entidad.setEstado(nuevoEstado);
    }

    @Transactional
    public void registrarNotaClinica(Long citaId, NotaEvolucionFormulario formulario) {
        Cita entidad = obtenerPorId(citaId);
        entidad.setNotaEvolucion(TextoNormalizadorUtil.opcional(formulario.getNotaEvolucion()));
        entidad.setDiagnosticoReferencial(TextoNormalizadorUtil.opcional(formulario.getDiagnosticoReferencial()));
        entidad.setEstado(formulario.getEstado());
    }

    private void validarDisponibilidad(
            Long psicologoId,
            LocalDate fecha,
            LocalTime horaInicio,
            int duracionMinutos,
            Long citaActualId) {
        List<Cita> citasDelDia = repositorio.findByPsicologoIdAndFechaAndEstadoNotOrderByHoraInicioAsc(
                psicologoId, fecha, EstadoCita.CANCELADA);
        LocalTime horaFin = horaInicio.plusMinutes(duracionMinutos);

        boolean hayTraslape = citasDelDia.stream()
                .filter(existente -> !Objects.equals(existente.getId(), citaActualId))
                .anyMatch(existente -> seTraslapan(horaInicio, horaFin, existente.getHoraInicio(), existente.getHoraFin()));

        if (hayTraslape) {
            List<LocalTime> alternativas = sugerirHorariosAlternativos(citasDelDia, citaActualId, duracionMinutos);
            throw new ConflictoHorarioException(
                    "El profesional ya tiene una cita reservada en ese horario. Selecciona una alternativa disponible.",
                    alternativas);
        }
    }

    private List<LocalTime> sugerirHorariosAlternativos(
            List<Cita> citasDelDia,
            Long citaActualId,
            int duracionMinutos) {
        List<LocalTime> sugerencias = new ArrayList<>();
        LocalTime candidato = HORA_APERTURA;
        while (!candidato.isAfter(HORA_CIERRE) && sugerencias.size() < 3) {
            LocalTime inicioCandidato = candidato;
            LocalTime finCandidato = inicioCandidato.plusMinutes(duracionMinutos);
            boolean ocupado = citasDelDia.stream()
                    .filter(existente -> !Objects.equals(existente.getId(), citaActualId))
                    .anyMatch(existente -> seTraslapan(
                            inicioCandidato, finCandidato, existente.getHoraInicio(), existente.getHoraFin()));
            if (!ocupado) {
                sugerencias.add(inicioCandidato);
            }
            candidato = candidato.plusMinutes(60);
        }
        return sugerencias;
    }

    private boolean seTraslapan(LocalTime inicioA, LocalTime finA, LocalTime inicioB, LocalTime finB) {
        return inicioA.isBefore(finB) && inicioB.isBefore(finA);
    }

    private boolean coincideBusqueda(Cita cita, String filtro) {
        String paciente = (cita.getPaciente().getNombres() + " " + cita.getPaciente().getApellidos()
                + " " + cita.getPaciente().getNumeroDocumento()).toLowerCase(Locale.ROOT);
        String psicologo = (cita.getPsicologo().getNombres() + " " + cita.getPsicologo().getApellidos())
                .toLowerCase(Locale.ROOT);
        String servicio = cita.getServicio().getNombre().toLowerCase(Locale.ROOT);
        return paciente.contains(filtro) || psicologo.contains(filtro) || servicio.contains(filtro);
    }
}
