package pe.com.sikalma.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministradorController {

    @GetMapping("/admin/inicio")
    public String inicio() {
        return "administracion/inicio";
    }

    @GetMapping("/admin/citas")
    public String citas() {
        return "administracion/citas";
    }
}
