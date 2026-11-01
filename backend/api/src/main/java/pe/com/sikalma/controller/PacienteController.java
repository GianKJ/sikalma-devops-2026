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

import pe.com.sikalma.dto.PacienteFormulario;
import pe.com.sikalma.entity.TipoDocumento;
import pe.com.sikalma.exception.RegistroDuplicadoException;
import pe.com.sikalma.service.PacienteServicio;
import pe.com.sikalma.validation.ValidacionDocumento;

@Controller
@RequestMapping("/admin/pacientes")
public class PacienteController {

    private static final String VISTA_FORMULARIO = "administracion/pacientes/formulario";
    private static final String REDIRECCION_PACIENTES = "redirect:/admin/pacientes";

    private final PacienteServicio servicio;

    public PacienteController(PacienteServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model modelo) {
        modelo.addAttribute("pacientes", servicio.listar(buscar));
        modelo.addAttribute("buscar", buscar == null ? "" : buscar);
        return "administracion/pacientes/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model modelo) {
        prepararFormulario(modelo, new PacienteFormulario());
        return VISTA_FORMULARIO;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model modelo) {
        prepararFormulario(modelo, servicio.obtenerFormulario(id));
        return VISTA_FORMULARIO;
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("formulario") PacienteFormulario formulario,
            BindingResult errores,
            Model modelo,
            RedirectAttributes redireccion) {
        ValidacionDocumento.validarDni(formulario.getTipoDocumento(), formulario.getNumeroDocumento(), errores);
        if (errores.hasErrors()) {
            prepararFormulario(modelo, formulario);
            return VISTA_FORMULARIO;
        }

        try {
            servicio.guardar(formulario);
        } catch (RegistroDuplicadoException excepcion) {
            errores.rejectValue(excepcion.getCampo(), "registro.duplicado", excepcion.getMessage());
            prepararFormulario(modelo, formulario);
            return VISTA_FORMULARIO;
        }

        redireccion.addFlashAttribute("mensaje", "Paciente guardado correctamente.");
        return REDIRECCION_PACIENTES;
    }

    @PostMapping("/{id}/estado")
    public String alternarEstado(@PathVariable Long id, RedirectAttributes redireccion) {
        servicio.alternarEstado(id);
        redireccion.addFlashAttribute("mensaje", "Estado del paciente actualizado.");
        return REDIRECCION_PACIENTES;
    }

    private void prepararFormulario(Model modelo, PacienteFormulario formulario) {
        modelo.addAttribute("formulario", formulario);
        modelo.addAttribute("tiposDocumento", TipoDocumento.values());
    }
}
