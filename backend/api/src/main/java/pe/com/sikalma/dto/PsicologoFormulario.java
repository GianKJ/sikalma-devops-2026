package pe.com.sikalma.dto;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pe.com.sikalma.entity.ModalidadAtencion;
import pe.com.sikalma.entity.TipoDocumento;

public class PsicologoFormulario {

    private Long id;

    @NotNull(message = "Selecciona un tipo de documento.")
    private TipoDocumento tipoDocumento;

    @NotBlank(message = "Ingresa el número de documento.")
    @Pattern(regexp = "^[A-Za-z0-9-]{6,20}$", message = "Ingresa un documento válido.")
    private String numeroDocumento;

    @NotBlank(message = "Ingresa los nombres.")
    @Size(max = 100, message = "Los nombres admiten hasta 100 caracteres.")
    private String nombres;

    @NotBlank(message = "Ingresa los apellidos.")
    @Size(max = 120, message = "Los apellidos admiten hasta 120 caracteres.")
    private String apellidos;

    @Size(max = 30, message = "La colegiatura admite hasta 30 caracteres.")
    private String colegiatura;

    @NotBlank(message = "Ingresa el teléfono.")
    @Pattern(regexp = "^[0-9+() -]{7,20}$", message = "Ingresa un teléfono válido.")
    private String telefono;

    @Email(message = "Ingresa un correo válido.")
    @Size(max = 160, message = "El correo admite hasta 160 caracteres.")
    private String correo;

    @Size(max = 1200, message = "La presentación admite hasta 1200 caracteres.")
    private String descripcionProfesional;

    @NotNull(message = "Selecciona una modalidad.")
    private ModalidadAtencion modalidad;

    private Set<Long> especialidadIds = new LinkedHashSet<>();

    private Set<Long> servicioIds = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getColegiatura() {
        return colegiatura;
    }

    public void setColegiatura(String colegiatura) {
        this.colegiatura = colegiatura;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDescripcionProfesional() {
        return descripcionProfesional;
    }

    public void setDescripcionProfesional(String descripcionProfesional) {
        this.descripcionProfesional = descripcionProfesional;
    }

    public ModalidadAtencion getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadAtencion modalidad) {
        this.modalidad = modalidad;
    }

    public Set<Long> getEspecialidadIds() {
        return especialidadIds;
    }

    public void setEspecialidadIds(Set<Long> especialidadIds) {
        this.especialidadIds = especialidadIds == null ? new LinkedHashSet<>() : especialidadIds;
    }

    public Set<Long> getServicioIds() {
        return servicioIds;
    }

    public void setServicioIds(Set<Long> servicioIds) {
        this.servicioIds = servicioIds == null ? new LinkedHashSet<>() : servicioIds;
    }
}
