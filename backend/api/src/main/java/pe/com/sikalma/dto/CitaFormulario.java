package pe.com.sikalma.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CitaFormulario {

    @NotNull(message = "Selecciona un paciente")
    private Long pacienteId;

    @NotNull(message = "Selecciona un psicólogo")
    private Long psicologoId;

    @NotNull(message = "Selecciona un servicio")
    private Long servicioId;

    @NotNull(message = "Indica la fecha y hora de inicio")
    @Future(message = "La fecha de inicio debe ser futura")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaHoraInicio;

    @NotNull(message = "Indica la fecha y hora de fin")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaHoraFin;

    @Size(max = 500, message = "Máximo 500 caracteres")
    private String observaciones;

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public Long getPsicologoId() { return psicologoId; }
    public void setPsicologoId(Long psicologoId) { this.psicologoId = psicologoId; }

    public Long getServicioId() { return servicioId; }
    public void setServicioId(Long servicioId) { this.servicioId = servicioId; }

    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public void setFechaHoraInicio(LocalDateTime v) { this.fechaHoraInicio = v; }

    public LocalDateTime getFechaHoraFin() { return fechaHoraFin; }
    public void setFechaHoraFin(LocalDateTime v) { this.fechaHoraFin = v; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}