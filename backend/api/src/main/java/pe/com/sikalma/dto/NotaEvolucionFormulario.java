package pe.com.sikalma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.com.sikalma.entity.EstadoCita;

public class NotaEvolucionFormulario {

    private Long citaId;

    @NotBlank(message = "Ingresa las observaciones clínicas de la sesión.")
    @Size(max = 1500, message = "La nota de evolución admite hasta 1500 caracteres.")
    private String notaEvolucion;

    @Size(max = 250, message = "El diagnóstico referencial admite hasta 250 caracteres.")
    private String diagnosticoReferencial;

    @NotNull(message = "Selecciona el estado resultante.")
    private EstadoCita estado = EstadoCita.ATENDIDA;

    public Long getCitaId() {
        return citaId;
    }

    public void setCitaId(Long citaId) {
        this.citaId = citaId;
    }

    public String getNotaEvolucion() {
        return notaEvolucion;
    }

    public void setNotaEvolucion(String notaEvolucion) {
        this.notaEvolucion = notaEvolucion;
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
}
