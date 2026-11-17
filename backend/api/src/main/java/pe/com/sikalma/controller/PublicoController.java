package pe.com.sikalma.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pe.com.sikalma.entity.Psicologo;
import pe.com.sikalma.service.InformacionPublicaServicio;
import pe.com.sikalma.service.PsicologoServicio;
import pe.com.sikalma.service.ServicioPsicologicoServicio;

@Controller
public class PublicoController {

    private static final String ATTR_SIKALMA = "sikalma";

    private final InformacionPublicaServicio informacionPublicaServicio;
    private final ServicioPsicologicoServicio servicioPsicologicoServicio;
    private final PsicologoServicio psicologoServicio;

    public PublicoController(
            InformacionPublicaServicio informacionPublicaServicio,
            ServicioPsicologicoServicio servicioPsicologicoServicio,
            PsicologoServicio psicologoServicio) {
        this.informacionPublicaServicio = informacionPublicaServicio;
        this.servicioPsicologicoServicio = servicioPsicologicoServicio;
        this.psicologoServicio = psicologoServicio;
    }

    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {
        model.addAttribute(ATTR_SIKALMA, informacionPublicaServicio.obtenerInformacionPublica());
        model.addAttribute("serviciosPublicos", servicioPsicologicoServicio.listarActivos());
        model.addAttribute("psicologosPublicos", psicologoServicio.listar(null).stream()
                .filter(Psicologo::isActivo)
                .toList());
        return "publico/inicio";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute(ATTR_SIKALMA, informacionPublicaServicio.obtenerInformacionPublica());
        return "autenticacion/login";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado(Model model) {
        model.addAttribute(ATTR_SIKALMA, informacionPublicaServicio.obtenerInformacionPublica());
        return "error/403";
    }
}
