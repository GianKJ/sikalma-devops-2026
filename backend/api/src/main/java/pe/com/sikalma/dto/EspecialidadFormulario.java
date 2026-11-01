package pe.com.sikalma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EspecialidadFormulario {

    private Long id;

    @NotBlank(message = "Ingresa el nombre de la especialidad.")
    @Size(max = 120, message = "El nombre admite hasta 120 caracteres.")
    private String nombre;

    @Size(max = 500, message = "La descripción admite hasta 500 caracteres.")
    private String descripcion;

    public String getDescripcion() {
        return this.descripcion;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Long getId() {
        return this.id;
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
