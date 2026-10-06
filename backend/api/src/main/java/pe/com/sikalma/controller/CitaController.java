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
import pe.com.sikalma.dto.CitaFormulario;
import pe.com.sikalma.entity.EstadoCita;
import pe.com.sikalma.repository.PacienteRepositorio;
import pe.com.sikalma.repository.PsicologoRepositorio;
import pe.com.sikalma.repository.ServicioRepositorio;
import pe.com.sikalma.service.CitaServicio;

@Controller
@RequestMapping("/citas")
public class CitaController {

    private final CitaServicio citaServicio;
    private final PacienteRepositorio pacienteRepositorio;
    private final PsicologoRepositorio psicologoRepositorio;
    private final ServicioRepositorio servicioRepositorio;

    public CitaController(CitaServicio citaServicio,
                          PacienteRepositorio pacienteRepositorio,
                          PsicologoRepositorio psicologoRepositorio,
                          ServicioRepositorio servicioRepositorio) {
        this.citaServicio = citaServicio;
        this.pacienteRepositorio = pacienteRepositorio;
        this.psicologoRepositorio = psicologoRepositorio;
        this.servicioRepositorio = servicioRepositorio;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("citas", citaServicio.listar());
        return "citas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("cita", new CitaFormulario());
        cargarCombos(model);
        return "citas/formulario";
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("cita") CitaFormulario form,
                          BindingResult result, Model model,
                          RedirectAttributes flash) {
        if (result.hasErrors()) {
            cargarCombos(model);
            return "citas/formulario";
        }
        try {
            citaServicio.registrar(form);
        } catch (IllegalArgumentException | IllegalStateException e) {
            result.reject("cita.invalida", e.getMessage());
            cargarCombos(model);
            return "citas/formulario";
        }
        flash.addFlashAttribute("mensaje", "Cita registrada correctamente");
        return "redirect:/citas";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam EstadoCita estado,
                                RedirectAttributes flash) {
        citaServicio.cambiarEstado(id, estado);
        flash.addFlashAttribute("mensaje", "Estado actualizado");
        return "redirect:/citas";
    }

    private void cargarCombos(Model model) {
        model.addAttribute("pacientes", pacienteRepositorio.findAll());
        model.addAttribute("psicologos", psicologoRepositorio.findAll());
        model.addAttribute("servicios", servicioRepositorio.findAll());
    }
}