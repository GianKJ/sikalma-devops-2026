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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import pe.com.sikalma.dto.PacienteFormulario;
import pe.com.sikalma.entity.TipoDocumento;
import pe.com.sikalma.service.PacienteServicio;
import pe.com.sikalma.validation.ValidacionDocumento;

@Controller
@RequestMapping({"/admin/pacientes", "/recepcion/pacientes"})
public class PacienteController {

    private static final String RUTA_ADMIN = "/admin/pacientes";
    private static final String RUTA_RECEPCION = "/recepcion/pacientes";
    private static final String VISTA_FORMULARIO = "administracion/pacientes/formulario";

    private final PacienteServicio servicio;

    public PacienteController(PacienteServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) String buscar,
            HttpServletRequest request,
            Model modelo) {
        modelo.addAttribute("pacientes", servicio.listar(buscar));
        modelo.addAttribute("buscar", buscar == null ? "" : buscar);
        modelo.addAttribute("rutaBasePacientes", ControladorUtil.resolverBaseRuta(request, RUTA_ADMIN, RUTA_RECEPCION));
        return "administracion/pacientes/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(HttpServletRequest request, Model modelo) {
        prepararFormulario(request, modelo, new PacienteFormulario());
        return VISTA_FORMULARIO;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, HttpServletRequest request, Model modelo) {
        prepararFormulario(request, modelo, servicio.obtenerFormulario(id));
        return VISTA_FORMULARIO;
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("formulario") PacienteFormulario formulario,
            BindingResult errores,
            HttpServletRequest request,
            Model modelo,
            RedirectAttributes redireccion) {
        ValidacionDocumento.validarDni(formulario.getTipoDocumento(), formulario.getNumeroDocumento(), errores);
        String rutaBase = ControladorUtil.resolverBaseRuta(request, RUTA_ADMIN, RUTA_RECEPCION);
        return ControladorUtil.procesarGuardadoConDuplicado(
                () -> servicio.guardar(formulario),
                errores,
                redireccion,
                "Paciente guardado correctamente.",
                rutaBase,
                () -> {
                    prepararFormulario(request, modelo, formulario);
                    return VISTA_FORMULARIO;
                });
    }

    @PostMapping("/{id}/estado")
    public String alternarEstado(
            @PathVariable Long id,
            HttpServletRequest request,
            RedirectAttributes redireccion) {
        servicio.alternarEstado(id);
        redireccion.addFlashAttribute("mensaje", "Estado del paciente actualizado.");
        return "redirect:" + ControladorUtil.resolverBaseRuta(request, RUTA_ADMIN, RUTA_RECEPCION);
    }

    private void prepararFormulario(HttpServletRequest request, Model modelo, PacienteFormulario formulario) {
        modelo.addAttribute("formulario", formulario);
        modelo.addAttribute("tiposDocumento", TipoDocumento.values());
        modelo.addAttribute("rutaBasePacientes", ControladorUtil.resolverBaseRuta(request, RUTA_ADMIN, RUTA_RECEPCION));
    }
}
