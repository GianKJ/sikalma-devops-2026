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
}
