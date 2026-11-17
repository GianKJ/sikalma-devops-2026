package pe.com.sikalma.controller;

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

import jakarta.validation.Valid;
import pe.com.sikalma.dto.UsuarioFormulario;
import pe.com.sikalma.entity.Psicologo;
import pe.com.sikalma.entity.RolUsuario;
import pe.com.sikalma.service.PsicologoServicio;
import pe.com.sikalma.service.UsuarioSistemaServicio;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioSistemaController {

    private static final String RUTA_USUARIOS = "/admin/usuarios";
    private static final String VISTA_FORMULARIO = "administracion/usuarios/formulario";

    private final UsuarioSistemaServicio servicio;
    private final PsicologoServicio psicologoServicio;

    public UsuarioSistemaController(UsuarioSistemaServicio servicio, PsicologoServicio psicologoServicio) {
        this.servicio = servicio;
        this.psicologoServicio = psicologoServicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model modelo) {
        modelo.addAttribute("usuarios", servicio.listar(buscar));
        modelo.addAttribute("buscar", buscar == null ? "" : buscar);
        return "administracion/usuarios/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model modelo) {
        prepararFormulario(modelo, new UsuarioFormulario());
        return VISTA_FORMULARIO;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model modelo) {
        prepararFormulario(modelo, servicio.obtenerFormulario(id));
        return VISTA_FORMULARIO;
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("formulario") UsuarioFormulario formulario,
            BindingResult errores,
            Model modelo,
            RedirectAttributes redireccion) {
        return ControladorUtil.procesarGuardadoConDuplicado(
                () -> servicio.guardar(formulario),
                errores,
                redireccion,
                "Usuario del sistema guardado correctamente.",
                RUTA_USUARIOS,
                () -> {
                    prepararFormulario(modelo, formulario);
                    return VISTA_FORMULARIO;
                });
    }

    @PostMapping("/{id}/estado")
    public String alternarEstado(@PathVariable Long id, RedirectAttributes redireccion) {
        servicio.alternarEstado(id);
        redireccion.addFlashAttribute("mensaje", "Estado de acceso del usuario actualizado.");
        return "redirect:" + RUTA_USUARIOS;
    }

    private void prepararFormulario(Model modelo, UsuarioFormulario formulario) {
        modelo.addAttribute("formulario", formulario);
        modelo.addAttribute("roles", RolUsuario.values());
        modelo.addAttribute("psicologos", psicologoServicio.listar(null).stream().filter(Psicologo::isActivo).toList());
    }
}
