package pe.com.sikalma.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.sikalma.dto.CitaFormulario;
import pe.com.sikalma.entity.Cita;
import pe.com.sikalma.entity.EstadoCita;
import pe.com.sikalma.repository.CitaRepositorio;
import pe.com.sikalma.repository.PacienteRepositorio;
import pe.com.sikalma.repository.PsicologoRepositorio;
import pe.com.sikalma.repository.ServicioRepositorio;

@Service
public class CitaServicio {

    private final CitaRepositorio citaRepositorio;
    private final PacienteRepositorio pacienteRepositorio;
    private final PsicologoRepositorio psicologoRepositorio;
    private final ServicioRepositorio servicioRepositorio;

    public CitaServicio(CitaRepositorio citaRepositorio,
                        PacienteRepositorio pacienteRepositorio,
                        PsicologoRepositorio psicologoRepositorio,
                        ServicioRepositorio servicioRepositorio) {
        this.citaRepositorio = citaRepositorio;
        this.pacienteRepositorio = pacienteRepositorio;
        this.psicologoRepositorio = psicologoRepositorio;
        this.servicioRepositorio = servicioRepositorio;
    }

    @Transactional(readOnly = true)
    public List<Cita> listar() {
        return citaRepositorio.listarConDetalle();
    }

    @Transactional
    public Cita registrar(CitaFormulario form) {
        if (!form.getFechaHoraFin().isAfter(form.getFechaHoraInicio())) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la de inicio");
        }
        if (citaRepositorio.existeCruce(form.getPsicologoId(),
                form.getFechaHoraInicio(), form.getFechaHoraFin(), EstadoCita.CANCELADA)) {
            throw new IllegalStateException("El psicólogo ya tiene una cita en ese horario");
        }

        Cita cita = new Cita();
        cita.setPaciente(pacienteRepositorio.findById(form.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado")));
        cita.setPsicologo(psicologoRepositorio.findById(form.getPsicologoId())
                .orElseThrow(() -> new IllegalArgumentException("Psicólogo no encontrado")));
        cita.setServicio(servicioRepositorio.findById(form.getServicioId())
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado")));
        cita.setFechaHoraInicio(form.getFechaHoraInicio());
        cita.setFechaHoraFin(form.getFechaHoraFin());
        cita.setObservaciones(form.getObservaciones());
        return citaRepositorio.save(cita);
    }

    @Transactional
    public void cambiarEstado(Long id, EstadoCita nuevoEstado) {
        Cita cita = citaRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        cita.setEstado(nuevoEstado);
    }
}