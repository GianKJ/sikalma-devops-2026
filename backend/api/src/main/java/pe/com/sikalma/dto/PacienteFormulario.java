package pe.com.sikalma.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pe.com.sikalma.entity.TipoDocumento;

public class PacienteFormulario {

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

    @NotNull(message = "Ingresa la fecha de nacimiento.")
    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura.")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "Ingresa el teléfono.")
    @Pattern(regexp = "^[0-9+() -]{7,20}$", message = "Ingresa un teléfono válido.")
    private String telefono;

    @Email(message = "Ingresa un correo válido.")
    @Size(max = 160, message = "El correo admite hasta 160 caracteres.")
    private String correo;

    @Size(max = 250, message = "La dirección admite hasta 250 caracteres.")
    private String direccion;

    @Size(max = 180, message = "El contacto admite hasta 180 caracteres.")
    private String contactoEmergencia;

    @Size(max = 180, message = "El apoderado admite hasta 180 caracteres.")
    private String apoderado;

    @Size(max = 1000, message = "Las observaciones admiten hasta 1000 caracteres.")
    private String observacionesAdministrativas;

    private boolean consentimientoRegistrado;

    @Size(max = 500, message = "Las preferencias admiten hasta 500 caracteres.")
    private String preferenciasPrivacidad;

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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContactoEmergencia() {
        return contactoEmergencia;
    }

    public void setContactoEmergencia(String contactoEmergencia) {
        this.contactoEmergencia = contactoEmergencia;
    }

    public String getApoderado() {
        return apoderado;
    }

    public void setApoderado(String apoderado) {
        this.apoderado = apoderado;
    }

    public String getObservacionesAdministrativas() {
        return observacionesAdministrativas;
    }

    public void setObservacionesAdministrativas(String observacionesAdministrativas) {
        this.observacionesAdministrativas = observacionesAdministrativas;
    }

    public boolean isConsentimientoRegistrado() {
        return consentimientoRegistrado;
    }

    public void setConsentimientoRegistrado(boolean consentimientoRegistrado) {
        this.consentimientoRegistrado = consentimientoRegistrado;
    }

    public String getPreferenciasPrivacidad() {
        return preferenciasPrivacidad;
    }

    public void setPreferenciasPrivacidad(String preferenciasPrivacidad) {
        this.preferenciasPrivacidad = preferenciasPrivacidad;
    }
}
