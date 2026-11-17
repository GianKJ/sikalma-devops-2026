package pe.com.sikalma;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SeguridadRutasIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void permiteConsultarElSitioPublico() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("SIKALMA")));
    }

    @Test
    void permiteConsultarElFormularioDeIngreso() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Iniciar sesión")));
    }

    @Test
    void protegeLasRutasAdministrativas() throws Exception {
        mockMvc.perform(get("/admin/inicio"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @org.springframework.beans.factory.annotation.Autowired
    private pe.com.sikalma.service.UsuarioSistemaServicio usuarioSistemaServicio;

    @org.springframework.beans.factory.annotation.Autowired
    private pe.com.sikalma.repository.UsuarioSistemaRepositorio usuarioSistemaRepositorio;

    @Test
    void permiteIngresarConUsuarioAdministradorRegistradoYRedirigeAlPanel() throws Exception {
        if (usuarioSistemaRepositorio.findByUsernameIgnoreCase("adminsec").isEmpty()) {
            pe.com.sikalma.dto.UsuarioFormulario form = new pe.com.sikalma.dto.UsuarioFormulario();
            form.setUsername("adminsec");
            form.setNombreCompleto("Administrador Seguridad");
            form.setCorreo("adminsec@sikalma.test");
            form.setPassword("Seguridad123!");
            form.setRol(pe.com.sikalma.entity.RolUsuario.ADMINISTRADOR);
            form.setActivo(true);
            usuarioSistemaServicio.guardar(form);
        }

        org.springframework.security.core.userdetails.UserDetails detalles =
                userDetailsService.loadUserByUsername("adminsec");
        org.junit.jupiter.api.Assertions.assertNotNull(detalles);

        mockMvc.perform(formLogin().user("adminsec").password("Seguridad123!"))
                .andExpect(authenticated().withUsername("adminsec").withRoles("ADMINISTRADOR"))
                .andExpect(redirectedUrl("/admin/inicio"));
    }

    @Test
    void permiteAlAdministradorConsultarElPanelYCitas() throws Exception {
        mockMvc.perform(get("/admin/inicio").with(user("admin").roles("ADMINISTRADOR")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Gestión de SIKALMA")));

        mockMvc.perform(get("/admin/citas").with(user("admin").roles("ADMINISTRADOR")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Gestión de citas")));

        mockMvc.perform(get("/admin/pacientes").with(user("admin").roles("ADMINISTRADOR")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Gestión de pacientes")));

        mockMvc.perform(get("/admin/psicologos").with(user("admin").roles("ADMINISTRADOR")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Gestión de psicólogos")));

        mockMvc.perform(get("/admin/especialidades").with(user("admin").roles("ADMINISTRADOR")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Gestión de especialidades")));

        mockMvc.perform(get("/admin/servicios").with(user("admin").roles("ADMINISTRADOR")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Gestión de servicios")));
    }

    @Test
    void codificaLasContrasenasNuevasConBCrypt() {
        String codificada = passwordEncoder.encode("contrasena-ficticia");

        assertTrue(codificada.startsWith("{bcrypt}"));
        assertTrue(passwordEncoder.matches("contrasena-ficticia", codificada));
    }

    @Test
    void rechazaCon405LosMetodosNoPermitidos() throws Exception {
        mockMvc.perform(post("/inicio").with(csrf()))
                .andExpect(status().isMethodNotAllowed());
    }
}
