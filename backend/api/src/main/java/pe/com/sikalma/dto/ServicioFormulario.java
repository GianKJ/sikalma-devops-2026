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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ModalidadAtencion getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadAtencion modalidad) {
        this.modalidad = modalidad;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getPublicoObjetivo() {
        return publicoObjetivo;
    }

    public void setPublicoObjetivo(String publicoObjetivo) {
        this.publicoObjetivo = publicoObjetivo;
    }
}
