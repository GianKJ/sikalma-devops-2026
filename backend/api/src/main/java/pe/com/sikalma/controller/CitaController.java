package pe.com.sikalma.controller;

import java.time.LocalDate;
import java.time.LocalTime;

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
import pe.com.sikalma.dto.CitaFormulario;
import pe.com.sikalma.entity.EstadoCita;
import pe.com.sikalma.entity.ModalidadAtencion;
import pe.com.sikalma.entity.Paciente;
import pe.com.sikalma.entity.Psicologo;
import pe.com.sikalma.exception.ConflictoHorarioException;
import pe.com.sikalma.service.CitaServicio;
import pe.com.sikalma.service.PacienteServicio;
import pe.com.sikalma.service.PsicologoServicio;
import pe.com.sikalma.service.ServicioPsicologicoServicio;

@Controller
@RequestMapping({"/admin/citas", "/recepcion/citas"})
public class CitaController {

    private static final String RUTA_ADMIN = "/admin/citas";
    private static final String RUTA_RECEPCION = "/recepcion/citas";
    private static final String VISTA_FORMULARIO = "administracion/citas/formulario";
    private static final String ATTR_RUTA_BASE_CITAS = "rutaBaseCitas";

    private final CitaServicio citaServicio;
    private final PacienteServicio pacienteServicio;
    private final PsicologoServicio psicologoServicio;
    private final ServicioPsicologicoServicio servicioPsicologicoServicio;

    public CitaController(
            CitaServicio citaServicio,
            PacienteServicio pacienteServicio,
            PsicologoServicio psicologoServicio,
            ServicioPsicologicoServicio servicioPsicologicoServicio) {
        this.citaServicio = citaServicio;
        this.pacienteServicio = pacienteServicio;
        this.psicologoServicio = psicologoServicio;
        this.servicioPsicologicoServicio = servicioPsicologicoServicio;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false) EstadoCita estado,
            @RequestParam(required = false) Long psicologoId,
            HttpServletRequest request,
            Model modelo) {
        modelo.addAttribute("citas", citaServicio.listar(buscar, estado, psicologoId));
        modelo.addAttribute("buscar", buscar == null ? "" : buscar);
        modelo.addAttribute("estadoSeleccionado", estado);
        modelo.addAttribute("psicologoSeleccionado", psicologoId);
        modelo.addAttribute("estados", EstadoCita.values());
        modelo.addAttribute("psicologos", psicologoServicio.listar(null));
        modelo.addAttribute(ATTR_RUTA_BASE_CITAS, ControladorUtil.resolverBaseRuta(request, RUTA_ADMIN, RUTA_RECEPCION));
        return "administracion/citas";
    }

    @GetMapping("/nuevo")
    public String nuevo(HttpServletRequest request, Model modelo) {
        CitaFormulario formulario = new CitaFormulario();
        formulario.setFecha(LocalDate.now());
        formulario.setHoraInicio(LocalTime.of(10, 0));
        prepararFormulario(request, modelo, formulario);
        return VISTA_FORMULARIO;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, HttpServletRequest request, Model modelo) {
        prepararFormulario(request, modelo, citaServicio.obtenerFormulario(id));
        return VISTA_FORMULARIO;
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("formulario") CitaFormulario formulario,
            BindingResult errores,
            HttpServletRequest request,
            Model modelo,
            RedirectAttributes redireccion) {
        String rutaBase = ControladorUtil.resolverBaseRuta(request, RUTA_ADMIN, RUTA_RECEPCION);
        if (!errores.hasErrors()) {
            try {
                citaServicio.guardar(formulario);
                redireccion.addFlashAttribute("mensaje", "Cita registrada y validada en agenda sin cruces de horario.");
                return "redirect:" + rutaBase;
            } catch (ConflictoHorarioException excepcion) {
                modelo.addAttribute("errorConflicto", excepcion.getMessage());
                modelo.addAttribute("horariosAlternativos", excepcion.getHorariosAlternativos());
            }
        }
        prepararFormulario(request, modelo, formulario);
        return VISTA_FORMULARIO;
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoCita estado,
            HttpServletRequest request,
            RedirectAttributes redireccion) {
        citaServicio.cambiarEstado(id, estado);
        redireccion.addFlashAttribute("mensaje", "Estado de la cita actualizado a " + estado.getEtiqueta() + ".");
        return "redirect:" + ControladorUtil.resolverBaseRuta(request, RUTA_ADMIN, RUTA_RECEPCION);
    }

    @GetMapping("/{id}/comprobante")
    public String comprobante(@PathVariable Long id, HttpServletRequest request, Model modelo) {
        modelo.addAttribute("cita", citaServicio.obtenerPorId(id));
        modelo.addAttribute(ATTR_RUTA_BASE_CITAS, ControladorUtil.resolverBaseRuta(request, RUTA_ADMIN, RUTA_RECEPCION));
        return "administracion/citas/comprobante";
    }

    private void prepararFormulario(HttpServletRequest request, Model modelo, CitaFormulario formulario) {
        modelo.addAttribute("formulario", formulario);
        modelo.addAttribute("pacientes", pacienteServicio.listar(null).stream().filter(Paciente::isActivo).toList());
        modelo.addAttribute("psicologos", psicologoServicio.listar(null).stream().filter(Psicologo::isActivo).toList());
        modelo.addAttribute("servicios", servicioPsicologicoServicio.listarActivos());
        modelo.addAttribute("modalidades", new ModalidadAtencion[]{ModalidadAtencion.PRESENCIAL, ModalidadAtencion.VIRTUAL});
        modelo.addAttribute("estados", EstadoCita.values());
        modelo.addAttribute(ATTR_RUTA_BASE_CITAS, ControladorUtil.resolverBaseRuta(request, RUTA_ADMIN, RUTA_RECEPCION));
    }
}
