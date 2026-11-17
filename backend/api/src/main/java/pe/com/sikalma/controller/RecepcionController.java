package pe.com.sikalma.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import pe.com.sikalma.entity.RolUsuario;
import pe.com.sikalma.service.DashboardMetricasServicio;

@Controller
@RequestMapping("/recepcion")
public class RecepcionController {

    private final DashboardMetricasServicio metricasServicio;

    public RecepcionController(DashboardMetricasServicio metricasServicio) {
        this.metricasServicio = metricasServicio;
    }

    @GetMapping("/inicio")
    public String inicio(HttpServletRequest request, Model modelo) {
        modelo.addAttribute("metricas", metricasServicio.calcularMetricas());
        String nombreVisible = (String) modelo.getAttribute("usuarioNombre");
        ControladorUtil.prepararBienvenidaSiPendiente(
                request,
                modelo,
                nombreVisible != null ? nombreVisible : "Recepción",
                RolUsuario.RECEPCIONISTA);
        return "recepcion/inicio";
    }
}
