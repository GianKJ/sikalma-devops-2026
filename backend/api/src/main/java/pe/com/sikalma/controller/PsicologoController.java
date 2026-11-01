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

import pe.com.sikalma.dto.PsicologoFormulario;
import pe.com.sikalma.entity.ModalidadAtencion;
import pe.com.sikalma.entity.TipoDocumento;
import pe.com.sikalma.exception.RegistroDuplicadoException;
import pe.com.sikalma.service.EspecialidadServicio;
import pe.com.sikalma.service.PsicologoServicio;
import pe.com.sikalma.service.ServicioPsicologicoServicio;
import pe.com.sikalma.validation.ValidacionDocumento;

@Controller
@RequestMapping("/admin/psicologos")
public class PsicologoController {

    private static final String VISTA_FORMULARIO = "administracion/psicologos/formulario";
    private static final String REDIRECCION_PSICOLOGOS = "redirect:/admin/psicologos";

    private final PsicologoServicio servicio;
    private final EspecialidadServicio especialidadServicio;
    private final ServicioPsicologicoServicio servicioPsicologicoServicio;

    public PsicologoController(
            PsicologoServicio servicio,
            EspecialidadServicio especialidadServicio,
            ServicioPsicologicoServicio servicioPsicologicoServicio) {
        this.servicio = servicio;
        this.especialidadServicio = especialidadServicio;
        this.servicioPsicologicoServicio = servicioPsicologicoServicio;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model modelo) {
        modelo.addAttribute("psicologos", servicio.listar(buscar));
        modelo.addAttribute("buscar", buscar == null ? "" : buscar);
        return "administracion/psicologos/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model modelo) {
        prepararFormulario(modelo, new PsicologoFormulario());
        return VISTA_FORMULARIO;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model modelo) {
        prepararFormulario(modelo, servicio.obtenerFormulario(id));
        return VISTA_FORMULARIO;
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("formulario") PsicologoFormulario formulario,
            BindingResult errores,
            Model modelo,
            RedirectAttributes redireccion) {
        ValidacionDocumento.validarDni(formulario.getTipoDocumento(), formulario.getNumeroDocumento(), errores);
        if (!errores.hasErrors()) {
            try {
                servicio.guardar(formulario);
                redireccion.addFlashAttribute("mensaje", "Psicólogo guardado correctamente.");
                return REDIRECCION_PSICOLOGOS;
            } catch (RegistroDuplicadoException excepcion) {
                errores.rejectValue(excepcion.getCampo(), "registro.duplicado", excepcion.getMessage());
            }
        }
        prepararFormulario(modelo, formulario);
        return VISTA_FORMULARIO;
    }

    @PostMapping("/{id}/estado")
    public String alternarEstado(@PathVariable Long id, RedirectAttributes redireccion) {
        servicio.alternarEstado(id);
        redireccion.addFlashAttribute("mensaje", "Estado del psicólogo actualizado.");
        return REDIRECCION_PSICOLOGOS;
    }

    private void prepararFormulario(Model modelo, PsicologoFormulario formulario) {
        modelo.addAttribute("formulario", formulario);
        modelo.addAttribute("tiposDocumento", TipoDocumento.values());
        modelo.addAttribute("modalidades", ModalidadAtencion.values());
        modelo.addAttribute("especialidades", especialidadServicio.listarActivas());
        modelo.addAttribute("servicios", servicioPsicologicoServicio.listarActivos());
    }
}
