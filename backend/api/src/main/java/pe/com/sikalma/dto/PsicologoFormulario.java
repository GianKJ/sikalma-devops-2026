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

    @NotBlank(message = "Ingresa los nombres del psicólogo.")
    @Size(max = 100, message = "Los nombres admiten hasta 100 caracteres.")
    private String nombres;

    @NotBlank(message = "Ingresa los apellidos del psicólogo.")
    @Size(max = 120, message = "Los apellidos admiten hasta 120 caracteres.")
    private String apellidos;

    @Size(max = 30, message = "La colegiatura admite hasta 30 caracteres.")
    private String colegiatura;

    @NotNull(message = "Selecciona un tipo de documento.")
    private TipoDocumento tipoDocumento;

    @NotBlank(message = "Ingresa el número de documento de identidad.")
    @Pattern(regexp = "^[A-Za-z0-9-]{6,20}$", message = "Ingresa un documento válido.")
    private String numeroDocumento;

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

    public Set<Long> getServicioIds() {
        return this.servicioIds;
    }

    public void setServicioIds(Set<Long> idsServicios) {
        this.servicioIds = idsServicios == null ? new LinkedHashSet<>() : idsServicios;
    }

    public Set<Long> getEspecialidadIds() {
        return this.especialidadIds;
    }

    public void setEspecialidadIds(Set<Long> idsEspecialidades) {
        this.especialidadIds = idsEspecialidades == null ? new LinkedHashSet<>() : idsEspecialidades;
    }

    public ModalidadAtencion getModalidad() {
        return this.modalidad;
    }

    public void setModalidad(ModalidadAtencion valorModalidad) {
        this.modalidad = valorModalidad;
    }

    public String getDescripcionProfesional() {
        return this.descripcionProfesional;
    }

    public void setDescripcionProfesional(String valorDescripcion) {
        this.descripcionProfesional = valorDescripcion;
    }

    public String getColegiatura() {
        return this.colegiatura;
    }

    public void setColegiatura(String valorColegiatura) {
        this.colegiatura = valorColegiatura;
    }

    public String getCorreo() {
        return this.correo;
    }

    public void setCorreo(String valorCorreo) {
        this.correo = valorCorreo;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long valorId) {
        this.id = valorId;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String valorTelefono) {
        this.telefono = valorTelefono;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public void setApellidos(String valorApellidos) {
        this.apellidos = valorApellidos;
    }

    public TipoDocumento getTipoDocumento() {
        return this.tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento valorTipoDoc) {
        this.tipoDocumento = valorTipoDoc;
    }

    public String getNombres() {
        return this.nombres;
    }

    public void setNombres(String valorNombres) {
        this.nombres = valorNombres;
    }

    public String getNumeroDocumento() {
        return this.numeroDocumento;
    }

    public void setNumeroDocumento(String valorNumeroDoc) {
        this.numeroDocumento = valorNumeroDoc;
    }
}
