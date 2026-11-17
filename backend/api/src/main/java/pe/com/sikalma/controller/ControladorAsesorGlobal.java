package pe.com.sikalma.controller;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import pe.com.sikalma.entity.RolUsuario;
import pe.com.sikalma.entity.UsuarioSistema;
import pe.com.sikalma.service.UsuarioDetallesServicio;

@ControllerAdvice
public class ControladorAsesorGlobal {

    private final UsuarioDetallesServicio usuarioDetallesServicio;

    public ControladorAsesorGlobal(UsuarioDetallesServicio usuarioDetallesServicio) {
        this.usuarioDetallesServicio = usuarioDetallesServicio;
    }

    @ModelAttribute
    public void agregarContextoUsuario(Authentication authentication, Model modelo) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            modelo.addAttribute("usuarioAutenticado", false);
            return;
        }

        String username = authentication.getName();
        Optional<UsuarioSistema> usuarioOpt = usuarioDetallesServicio.buscarPorUsername(username);
        RolUsuario rol = usuarioOpt.map(UsuarioSistema::getRol)
                .orElseGet(() -> resolverRolDesdeAutoridades(authentication));
        String nombreVisible = usuarioOpt.map(UsuarioSistema::getNombreCompleto).orElse(username);

        modelo.addAttribute("usuarioAutenticado", true);
        modelo.addAttribute("usuarioUsername", username);
        modelo.addAttribute("usuarioNombre", nombreVisible);
        modelo.addAttribute("usuarioRol", rol);
        modelo.addAttribute("usuarioRolEtiqueta", rol.getEtiqueta());
        modelo.addAttribute("usuarioRutaInicio", rol.getRutaInicio());
        modelo.addAttribute("esAdministrador", rol == RolUsuario.ADMINISTRADOR);
        modelo.addAttribute("esRecepcionista", rol == RolUsuario.RECEPCIONISTA);
        modelo.addAttribute("esProfesional", rol == RolUsuario.PSICOLOGO || rol == RolUsuario.FISIOTERAPEUTA);
    }

    private RolUsuario resolverRolDesdeAutoridades(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String nombre = authority.getAuthority();
            if (nombre != null && nombre.startsWith("ROLE_")) {
                String codigo = nombre.substring(5);
                for (RolUsuario rol : RolUsuario.values()) {
                    if (rol.name().equals(codigo)) {
                        return rol;
                    }
                }
            }
        }
        return RolUsuario.ADMINISTRADOR;
    }
}
