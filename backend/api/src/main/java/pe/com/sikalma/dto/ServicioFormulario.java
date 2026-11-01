package pe.com.sikalma.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pe.com.sikalma.entity.ModalidadAtencion;

public class ServicioFormulario {

    private Long id;

    @NotBlank(message = "Ingresa el nombre del servicio.")
    @Size(max = 140, message = "El nombre admite hasta 140 caracteres.")
    private String nombre;

    @NotBlank(message = "Ingresa una descripción.")
    @Size(max = 1000, message = "La descripción admite hasta 1000 caracteres.")
    private String descripcion;

    @NotNull(message = "Selecciona una modalidad.")
    private ModalidadAtencion modalidad;

    @NotNull(message = "Ingresa la duración aproximada.")
    @Min(value = 15, message = "La duración mínima es 15 minutos.")
    @Max(value = 480, message = "La duración máxima es 480 minutos.")
    private Integer duracionMinutos;

    @NotBlank(message = "Indica el público objetivo.")
    @Size(max = 250, message = "El público objetivo admite hasta 250 caracteres.")
    private String publicoObjetivo;

    public String getPublicoObjetivo() {
        return this.publicoObjetivo;
    }

    public Integer getDuracionMinutos() {
        return this.duracionMinutos;
    }

    public ModalidadAtencion getModalidad() {
        return this.modalidad;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Long getId() {
        return this.id;
    }

    public void setPublicoObjetivo(String valorPublico) {
        this.publicoObjetivo = valorPublico;
    }

    public void setDuracionMinutos(Integer valorDuracion) {
        this.duracionMinutos = valorDuracion;
    }

    public void setModalidad(ModalidadAtencion valorModalidad) {
        this.modalidad = valorModalidad;
    }

    public void setDescripcion(String valorDescripcion) {
        this.descripcion = valorDescripcion;
    }

    public void setNombre(String valorNombre) {
        this.nombre = valorNombre;
    }

    public void setId(Long valorId) {
        this.id = valorId;
    }
}
