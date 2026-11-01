package pe.com.sikalma.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "psicologos")
public class Psicologo extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 120)
    private String apellidos;

    @Column(length = 30)
    private String colegiatura;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 20)
    private TipoDocumento tipoDocumento;

    @Column(name = "numero_documento", nullable = false, unique = true, length = 20)
    private String numeroDocumento;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(length = 160)
    private String correo;

    @Column(name = "descripcion_profesional", length = 1200)
    private String descripcionProfesional;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ModalidadAtencion modalidad;

    @Column(nullable = false)
    private boolean activo = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "psicologo_especialidad",
            joinColumns = @JoinColumn(name = "psicologo_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidad_id"))
    private Set<Especialidad> especialidades = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "psicologo_servicio",
            joinColumns = @JoinColumn(name = "psicologo_id"),
            inverseJoinColumns = @JoinColumn(name = "servicio_id"))
    private Set<Servicio> servicios = new LinkedHashSet<>();

    public Set<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(Set<Servicio> listaServicios) {
        this.servicios = listaServicios;
    }

    public Set<Especialidad> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(Set<Especialidad> listaEspecialidades) {
        this.especialidades = listaEspecialidades;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean estadoActivo) {
        this.activo = estadoActivo;
    }

    public ModalidadAtencion getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadAtencion tipoModalidad) {
        this.modalidad = tipoModalidad;
    }

    public String getDescripcionProfesional() {
        return descripcionProfesional;
    }

    public void setDescripcionProfesional(String resumenProfesional) {
        this.descripcionProfesional = resumenProfesional;
    }

    public String getColegiatura() {
        return colegiatura;
    }

    public void setColegiatura(String codigoColegiatura) {
        this.colegiatura = codigoColegiatura;
    }

    public Long getId() {
        return id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String direccionCorreo) {
        this.correo = direccionCorreo;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String documentoIdentidad) {
        this.numeroDocumento = documentoIdentidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String numeroTelefono) {
        this.telefono = numeroTelefono;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento claseDocumento) {
        this.tipoDocumento = claseDocumento;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidosCompletos) {
        this.apellidos = apellidosCompletos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombresCompletos) {
        this.nombres = nombresCompletos;
    }
}
