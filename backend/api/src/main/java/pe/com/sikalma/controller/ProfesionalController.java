package pe.com.sikalma.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import pe.com.sikalma.dto.NotaEvolucionFormulario;
import pe.com.sikalma.entity.Cita;
import pe.com.sikalma.entity.EstadoCita;
import pe.com.sikalma.entity.RolUsuario;
import pe.com.sikalma.entity.UsuarioSistema;
import pe.com.sikalma.service.CitaServicio;
import pe.com.sikalma.service.UsuarioDetallesServicio;

@Controller
@RequestMapping("/profesional")
public class ProfesionalController {

    private static final String VISTA_ATENCION = "profesional/atencion";

    private final CitaServicio citaServicio;
    private final UsuarioDetallesServicio usuarioDetallesServicio;

    public ProfesionalController(CitaServicio citaServicio, UsuarioDetallesServicio usuarioDetallesServicio) {
        this.citaServicio = citaServicio;
        this.usuarioDetallesServicio = usuarioDetallesServicio;
    }

    @GetMapping("/inicio")
    public String inicio(
            @RequestParam(required = false) EstadoCita estado,
            Authentication authentication,
            HttpServletRequest request,
            Model modelo) {
        UsuarioSistema usuario = usuarioDetallesServicio.buscarPorUsername(authentication.getName()).orElse(null);
        Long psicologoId = usuario != null && usuario.getPsicologo() != null
                ? usuario.getPsicologo().getId()
                : null;
        RolUsuario rol = usuario != null ? usuario.getRol() : RolUsuario.PSICOLOGO;
        String nombreVisible = usuario != null ? usuario.getNombreCompleto() : authentication.getName();

        List<Cita> agenda = citaServicio.listarParaProfesional(psicologoId, estado);
        long atendidas = agenda.stream().filter(c -> c.getEstado() == EstadoCita.ATENDIDA).count();
        long pendientesOConfirmadas = agenda.stream()
                .filter(c -> c.getEstado() == EstadoCita.PENDIENTE
                        || c.getEstado() == EstadoCita.CONFIRMADA
                        || c.getEstado() == EstadoCita.EN_SALA)
                .count();

        modelo.addAttribute("citas", agenda);
        modelo.addAttribute("totalAgenda", agenda.size());
        modelo.addAttribute("totalAtendidas", atendidas);
        modelo.addAttribute("totalPendientes", pendientesOConfirmadas);
        modelo.addAttribute("estadoSeleccionado", estado);
        modelo.addAttribute("estados", EstadoCita.values());
        modelo.addAttribute("profesionalVinculado", usuario != null ? usuario.getPsicologo() : null);

        ControladorUtil.prepararBienvenidaSiPendiente(request, modelo, nombreVisible, rol);
        return "profesional/inicio";
    }

    @GetMapping("/citas/{id}/atencion")
    public String editarAtencion(
            @PathVariable Long id,
            Authentication authentication,
            Model modelo) {
        Cita cita = verificarAccesoACita(id, authentication);
        modelo.addAttribute("cita", cita);
        modelo.addAttribute("formulario", citaServicio.obtenerFormularioNota(id));
        modelo.addAttribute("estadosClinicos", List.of(EstadoCita.EN_SALA, EstadoCita.ATENDIDA));
        return VISTA_ATENCION;
    }

    @PostMapping("/citas/{id}/atencion")
    public String guardarAtencion(
            @PathVariable Long id,
            @Valid @ModelAttribute("formulario") NotaEvolucionFormulario formulario,
            BindingResult errores,
            Authentication authentication,
            Model modelo,
            RedirectAttributes redireccion) {
        Cita cita = verificarAccesoACita(id, authentication);
        if (errores.hasErrors()) {
            modelo.addAttribute("cita", cita);
            modelo.addAttribute("estadosClinicos", List.of(EstadoCita.EN_SALA, EstadoCita.ATENDIDA));
            return VISTA_ATENCION;
        }
        citaServicio.registrarNotaClinica(id, formulario);
        redireccion.addFlashAttribute("mensaje", "Nota de evolución clínica registrada de forma confidencial.");
        return "redirect:/profesional/inicio";
    }

    private Cita verificarAccesoACita(Long citaId, Authentication authentication) {
        Cita cita = citaServicio.obtenerPorId(citaId);
        UsuarioSistema usuario = usuarioDetallesServicio.buscarPorUsername(authentication.getName()).orElse(null);
        if (usuario != null
                && usuario.getRol() != RolUsuario.ADMINISTRADOR
                && usuario.getPsicologo() != null
                && !Objects.equals(usuario.getPsicologo().getId(), cita.getPsicologo().getId())) {
            throw new AccessDeniedException("No puedes acceder a citas de otro profesional.");
        }
        return cita;
    }
}
