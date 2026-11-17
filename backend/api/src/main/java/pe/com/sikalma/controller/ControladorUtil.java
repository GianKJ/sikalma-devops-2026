package pe.com.sikalma.controller;

import java.util.function.Supplier;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import pe.com.sikalma.entity.RolUsuario;
import pe.com.sikalma.exception.RegistroDuplicadoException;
import pe.com.sikalma.security.ManejadorIngresoExitoso;

public final class ControladorUtil {

    private ControladorUtil() {
    }

    public static void prepararBienvenidaSiPendiente(
            HttpServletRequest request,
            Model modelo,
            String nombreUsuario,
            RolUsuario rol) {
        HttpSession session = request.getSession(false);
        boolean pendiente = session != null
                && Boolean.TRUE.equals(session.getAttribute(ManejadorIngresoExitoso.ATTR_BIENVENIDA_PENDIENTE));
        if (pendiente) {
            session.removeAttribute(ManejadorIngresoExitoso.ATTR_BIENVENIDA_PENDIENTE);
        }
        modelo.addAttribute("bienvenidaActiva", pendiente);
        modelo.addAttribute("bienvenidaNombre", nombreUsuario);
        modelo.addAttribute("bienvenidaRolCodigo", rol.name());
        modelo.addAttribute("bienvenidaRolEtiqueta", rol.getEtiqueta());
        modelo.addAttribute("bienvenidaDescripcion", rol.getDescripcionBienvenida());
    }

    public static String resolverBaseRuta(HttpServletRequest request, String rutaAdmin, String rutaRecepcion) {
        String path = request.getServletPath();
        if (path == null || path.isEmpty()) {
            path = request.getRequestURI();
        }
        String contexto = request.getContextPath();
        if (path != null && contexto != null && !contexto.isEmpty() && path.startsWith(contexto)) {
            path = path.substring(contexto.length());
        }
        return path != null && path.startsWith("/recepcion") ? rutaRecepcion : rutaAdmin;
    }

    public static String procesarGuardadoConDuplicado(
            Runnable accionGuardar,
            BindingResult errores,
            RedirectAttributes redireccion,
            String mensajeExito,
            String rutaRedireccion,
            Supplier<String> vistaError) {
        if (errores.hasErrors()) {
            return vistaError.get();
        }
        try {
            accionGuardar.run();
        } catch (RegistroDuplicadoException excepcion) {
            errores.rejectValue(excepcion.getCampo(), "registro.duplicado", excepcion.getMessage());
            return vistaError.get();
        }
        redireccion.addFlashAttribute("mensaje", mensajeExito);
        return "redirect:" + rutaRedireccion;
    }
}
