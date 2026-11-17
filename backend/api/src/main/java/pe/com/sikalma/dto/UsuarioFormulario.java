package pe.com.sikalma.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pe.com.sikalma.entity.RolUsuario;

public class UsuarioFormulario {

    private Long id;

    @NotBlank(message = "Ingresa un nombre de usuario.")
    @Pattern(regexp = "^[A-Za-z0-9._-]{3,60}$", message = "Usa entre 3 y 60 caracteres sin espacios.")
    private String username;

    @NotBlank(message = "Ingresa el nombre completo.")
    @Size(max = 160, message = "El nombre admite hasta 160 caracteres.")
    private String nombreCompleto;

    @Email(message = "Ingresa un correo electrónico válido.")
    @Size(max = 160, message = "El correo admite hasta 160 caracteres.")
    private String correo;

    @NotNull(message = "Selecciona un rol.")
    private RolUsuario rol = RolUsuario.RECEPCIONISTA;

    private Long psicologoId;

    @Size(max = 100, message = "La contraseña admite hasta 100 caracteres.")
    private String password;

    private boolean activo = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public Long getPsicologoId() {
        return psicologoId;
    }

    public void setPsicologoId(Long psicologoId) {
        this.psicologoId = psicologoId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
