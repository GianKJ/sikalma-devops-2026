package pe.com.sikalma.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pacientes")
public class Paciente extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 20)
    private TipoDocumento tipoDocumento;

    @Column(name = "numero_documento", nullable = false, unique = true, length = 20)
    private String numeroDocumento;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 120)
    private String apellidos;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(length = 160)
    private String correo;

    @Column(length = 250)
    private String direccion;

    @Column(name = "contacto_emergencia", length = 180)
    private String contactoEmergencia;

    @Column(length = 180)
    private String apoderado;

    @Column(name = "observaciones_administrativas", length = 1000)
    private String observacionesAdministrativas;

    @Column(name = "consentimiento_registrado", nullable = false)
    private boolean consentimientoRegistrado;

    @Column(name = "preferencias_privacidad", length = 500)
    private String preferenciasPrivacidad;

    @Column(nullable = false)
    private boolean activo = true;

    public Long getId() {
        return id;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
