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

    public String getPreferenciasPrivacidad() {
        return this.preferenciasPrivacidad;
    }

    public boolean isConsentimientoRegistrado() {
        return this.consentimientoRegistrado;
    }

    public String getObservacionesAdministrativas() {
        return this.observacionesAdministrativas;
    }

    public String getApoderado() {
        return this.apoderado;
    }

    public String getContactoEmergencia() {
        return this.contactoEmergencia;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public String getCorreo() {
        return this.correo;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public String getNombres() {
        return this.nombres;
    }

    public TipoDocumento getTipoDocumento() {
        return this.tipoDocumento;
    }

    public String getNumeroDocumento() {
        return this.numeroDocumento;
    }

    public Long getId() {
        return this.id;
    }

    public void setPreferenciasPrivacidad(String valorPreferencias) {
        this.preferenciasPrivacidad = valorPreferencias;
    }

    public void setConsentimientoRegistrado(boolean valorConsentimiento) {
        this.consentimientoRegistrado = valorConsentimiento;
    }

    public void setObservacionesAdministrativas(String valorObservaciones) {
        this.observacionesAdministrativas = valorObservaciones;
    }

    public void setApoderado(String valorApoderado) {
        this.apoderado = valorApoderado;
    }

    public void setContactoEmergencia(String valorContacto) {
        this.contactoEmergencia = valorContacto;
    }

    public void setDireccion(String valorDireccion) {
        this.direccion = valorDireccion;
    }

    public void setFechaNacimiento(LocalDate valorFecha) {
        this.fechaNacimiento = valorFecha;
    }

    public void setCorreo(String valorCorreo) {
        this.correo = valorCorreo;
    }

    public void setApellidos(String valorApellidos) {
        this.apellidos = valorApellidos;
    }

    public void setTelefono(String valorTelefono) {
        this.telefono = valorTelefono;
    }

    public void setNombres(String valorNombres) {
        this.nombres = valorNombres;
    }

    public void setTipoDocumento(TipoDocumento valorTipo) {
        this.tipoDocumento = valorTipo;
    }

    public void setNumeroDocumento(String valorNumero) {
        this.numeroDocumento = valorNumero;
    }

    public void setId(Long valorId) {
        this.id = valorId;
    }
}
