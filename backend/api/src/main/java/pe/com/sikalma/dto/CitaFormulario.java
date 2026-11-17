package pe.com.sikalma.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.com.sikalma.entity.EstadoCita;
import pe.com.sikalma.entity.ModalidadAtencion;

public class CitaFormulario {

    private Long id;

    @NotNull(message = "Selecciona un paciente.")
    private Long pacienteId;

    @NotNull(message = "Selecciona un profesional.")
    private Long psicologoId;

    @NotNull(message = "Selecciona un servicio.")
    private Long servicioId;

    @NotNull(message = "Selecciona la fecha de la cita.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @NotNull(message = "Selecciona la hora de inicio.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @NotNull(message = "Selecciona la modalidad.")
    private ModalidadAtencion modalidad = ModalidadAtencion.PRESENCIAL;

    @NotNull(message = "Selecciona el estado.")
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @Size(max = 500, message = "El motivo admite hasta 500 caracteres.")
    private String motivoConsulta;

    @Size(max = 1500, message = "La nota admite hasta 1500 caracteres.")
    private String notaEvolucion;

    @Size(max = 250, message = "El diagnóstico referencial admite hasta 250 caracteres.")
    private String diagnosticoReferencial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getPsicologoId() {
        return psicologoId;
    }

    public void setPsicologoId(Long psicologoId) {
        this.psicologoId = psicologoId;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public String getDiagnosticoReferencial() {
        return diagnosticoReferencial;
    }

    public void setDiagnosticoReferencial(String diagnosticoReferencial) {
        this.diagnosticoReferencial = diagnosticoReferencial;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public String getNotaEvolucion() {
        return notaEvolucion;
    }

    public void setNotaEvolucion(String notaEvolucion) {
        this.notaEvolucion = notaEvolucion;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public ModalidadAtencion getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadAtencion modalidad) {
        this.modalidad = modalidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
