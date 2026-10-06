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

import pe.com.sikalma.dto.ServicioFormulario;
import pe.com.sikalma.entity.ModalidadAtencion;
import pe.com.sikalma.exception.RegistroDuplicadoException;
import pe.com.sikalma.service.ServicioPsicologicoServicio;

@Controller
@RequestMapping("/admin/servicios")
public class ServicioController {

    private final ServicioPsicologicoServicio servicio;

    public ServicioController(ServicioPsicologicoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model modelo) {
        modelo.addAttribute("servicios", servicio.listar(buscar));
        modelo.addAttribute("buscar", buscar == null ? "" : buscar);
        return "administracion/servicios/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model modelo) {
        prepararFormulario(modelo, new ServicioFormulario());
        return "administracion/servicios/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model modelo) {
        prepararFormulario(modelo, servicio.obtenerFormulario(id));
        return "administracion/servicios/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("formulario") ServicioFormulario formulario,
            BindingResult errores,
            Model modelo,
            RedirectAttributes redireccion) {
        if (errores.hasErrors()) {
            prepararFormulario(modelo, formulario);
            return "administracion/servicios/formulario";
        }
        try {
            servicio.guardar(formulario);
        } catch (RegistroDuplicadoException excepcion) {
            errores.rejectValue(excepcion.getCampo(), "registro.duplicado", excepcion.getMessage());
            prepararFormulario(modelo, formulario);
            return "administracion/servicios/formulario";
        }
        redireccion.addFlashAttribute("mensaje", "Servicio guardado correctamente.");
        return "redirect:/admin/servicios";
    }

    @PostMapping("/{id}/estado")
    public String alternarEstado(@PathVariable Long id, RedirectAttributes redireccion) {
        servicio.alternarEstado(id);
        redireccion.addFlashAttribute("mensaje", "Estado del servicio actualizado.");
        return "redirect:/admin/servicios";
    }

    private void prepararFormulario(Model modelo, ServicioFormulario formulario) {
        modelo.addAttribute("formulario", formulario);
        modelo.addAttribute("modalidades", ModalidadAtencion.values());
    }
}
