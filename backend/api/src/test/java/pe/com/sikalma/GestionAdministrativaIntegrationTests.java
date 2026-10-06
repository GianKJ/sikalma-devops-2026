package pe.com.sikalma;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "admin", roles = "ADMINISTRADOR")
class GestionAdministrativaIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registraPacienteYLoMuestraEnElListado() throws Exception {
        mockMvc.perform(post("/admin/pacientes/guardar")
                        .with(csrf())
                        .param("tipoDocumento", "DNI")
                        .param("numeroDocumento", "70000001")
                        .param("nombres", "Paciente")
                        .param("apellidos", "Demostración")
                        .param("fechaNacimiento", "1998-04-12")
                        .param("telefono", "999111222")
                        .param("correo", "paciente.demo@sikalma.test")
                        .param("consentimientoRegistrado", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/pacientes"));

        mockMvc.perform(get("/admin/pacientes"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("70000001")))
                .andExpect(content().string(containsString("Paciente")));
    }

    @Test
    void registraCatalogosYPsicologo() throws Exception {
        mockMvc.perform(post("/admin/especialidades/guardar")
                        .with(csrf())
                        .param("nombre", "Psicología clínica de prueba")
                        .param("descripcion", "Especialidad ficticia para pruebas."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/especialidades"));

        mockMvc.perform(post("/admin/servicios/guardar")
                        .with(csrf())
                        .param("nombre", "Terapia de prueba")
                        .param("descripcion", "Servicio ficticio para prueba automatizada.")
                        .param("modalidad", "AMBAS")
                        .param("duracionMinutos", "60")
                        .param("publicoObjetivo", "Personas adultas"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/servicios"));

        mockMvc.perform(post("/admin/psicologos/guardar")
                        .with(csrf())
                        .param("tipoDocumento", "DNI")
                        .param("numeroDocumento", "70000002")
                        .param("nombres", "Profesional")
                        .param("apellidos", "Demostración")
                        .param("colegiatura", "CPSP-DEMO-01")
                        .param("telefono", "999333444")
                        .param("correo", "profesional.demo@sikalma.test")
                        .param("modalidad", "AMBAS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/psicologos"));

        mockMvc.perform(get("/admin/psicologos"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Profesional")));
        mockMvc.perform(get("/admin/especialidades"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Psicología clínica de prueba")));
        mockMvc.perform(get("/admin/servicios"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Terapia de prueba")));
    }

    @Test
    void muestraFormulariosDeAlta() throws Exception {
        mockMvc.perform(get("/admin/pacientes/nuevo"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Registrar paciente")));
        mockMvc.perform(get("/admin/psicologos/nuevo"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Registrar psicólogo")));
        mockMvc.perform(get("/admin/especialidades/nuevo"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Registrar especialidad")));
        mockMvc.perform(get("/admin/servicios/nuevo"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Registrar servicio")));
    }
}
