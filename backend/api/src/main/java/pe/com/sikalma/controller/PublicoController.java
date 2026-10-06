package pe.com.sikalma.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pe.com.sikalma.service.InformacionPublicaServicio;

@Controller
public class PublicoController {

    private final InformacionPublicaServicio informacionPublicaServicio;

    public PublicoController(InformacionPublicaServicio informacionPublicaServicio) {
        this.informacionPublicaServicio = informacionPublicaServicio;
    }

    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {
        model.addAttribute("sikalma", informacionPublicaServicio.obtenerInformacionPublica());
        return "publico/inicio";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("sikalma", informacionPublicaServicio.obtenerInformacionPublica());
        return "autenticacion/login";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado(Model model) {
        model.addAttribute("sikalma", informacionPublicaServicio.obtenerInformacionPublica());
        return "error/403";
    }
}
