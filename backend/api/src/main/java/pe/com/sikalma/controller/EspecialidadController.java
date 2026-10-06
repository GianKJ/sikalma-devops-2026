package pe.com.sikalma.controller;

import jakarta.validation.Valid;
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

import pe.com.sikalma.dto.EspecialidadFormulario;
import pe.com.sikalma.exception.RegistroDuplicadoException;
import pe.com.sikalma.service.EspecialidadServicio;

@Controller
@RequestMapping("/admin/especialidades")
public class EspecialidadController {

    private final EspecialidadServicio servicio;

    public EspecialidadController(EspecialidadServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model modelo) {
        modelo.addAttribute("especialidades", servicio.listar(buscar));
        modelo.addAttribute("buscar", buscar == null ? "" : buscar);
        return "administracion/especialidades/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model modelo) {
        modelo.addAttribute("formulario", new EspecialidadFormulario());
        return "administracion/especialidades/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model modelo) {
        modelo.addAttribute("formulario", servicio.obtenerFormulario(id));
        return "administracion/especialidades/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("formulario") EspecialidadFormulario formulario,
            BindingResult errores,
            RedirectAttributes redireccion) {
        if (errores.hasErrors()) {
            return "administracion/especialidades/formulario";
        }
        try {
            servicio.guardar(formulario);
        } catch (RegistroDuplicadoException excepcion) {
            errores.rejectValue(excepcion.getCampo(), "registro.duplicado", excepcion.getMessage());
            return "administracion/especialidades/formulario";
        }
        redireccion.addFlashAttribute("mensaje", "Especialidad guardada correctamente.");
        return "redirect:/admin/especialidades";
    }

    @PostMapping("/{id}/estado")
    public String alternarEstado(@PathVariable Long id, RedirectAttributes redireccion) {
        servicio.alternarEstado(id);
        redireccion.addFlashAttribute("mensaje", "Estado de la especialidad actualizado.");
        return "redirect:/admin/especialidades";
    }
}
