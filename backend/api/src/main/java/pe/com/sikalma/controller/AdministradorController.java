package pe.com.sikalma.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import pe.com.sikalma.entity.RolUsuario;
import pe.com.sikalma.service.DashboardMetricasServicio;

@Controller
public class AdministradorController {

    private final DashboardMetricasServicio metricasServicio;

    public AdministradorController(DashboardMetricasServicio metricasServicio) {
        this.metricasServicio = metricasServicio;
    }

    @GetMapping("/admin")
    public String raizAdmin() {
        return "redirect:/admin/inicio";
    }

    @GetMapping("/admin/inicio")
    public String inicio(HttpServletRequest request, Model modelo) {
        modelo.addAttribute("metricas", metricasServicio.calcularMetricas());
        String nombreVisible = (String) modelo.getAttribute("usuarioNombre");
        ControladorUtil.prepararBienvenidaSiPendiente(
                request,
                modelo,
                nombreVisible != null ? nombreVisible : "Administrador",
                RolUsuario.ADMINISTRADOR);
        return "administracion/inicio";
    }
}
