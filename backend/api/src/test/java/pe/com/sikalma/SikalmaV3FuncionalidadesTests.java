package pe.com.sikalma;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import pe.com.sikalma.dto.CitaFormulario;
import pe.com.sikalma.dto.NotaEvolucionFormulario;
import pe.com.sikalma.dto.ResumenMetricasDto;
import pe.com.sikalma.dto.UsuarioFormulario;
import pe.com.sikalma.entity.Cita;
import pe.com.sikalma.entity.EstadoCita;
import pe.com.sikalma.entity.ModalidadAtencion;
import pe.com.sikalma.entity.Paciente;
import pe.com.sikalma.entity.Psicologo;
import pe.com.sikalma.entity.RolUsuario;
import pe.com.sikalma.entity.Servicio;
import pe.com.sikalma.entity.UsuarioSistema;
import pe.com.sikalma.exception.ConflictoHorarioException;
import pe.com.sikalma.exception.RegistroDuplicadoException;
import pe.com.sikalma.exception.RegistroNoEncontradoException;
import pe.com.sikalma.repository.CitaRepositorio;
import pe.com.sikalma.repository.EspecialidadRepositorio;
import pe.com.sikalma.repository.PacienteRepositorio;
import pe.com.sikalma.repository.PsicologoRepositorio;
import pe.com.sikalma.repository.ServicioRepositorio;
import pe.com.sikalma.repository.UsuarioSistemaRepositorio;
import pe.com.sikalma.security.ManejadorIngresoExitoso;
import pe.com.sikalma.service.CitaServicio;
import pe.com.sikalma.service.DashboardMetricasServicio;
import pe.com.sikalma.service.InicializadorDatosDemo;
import pe.com.sikalma.service.PacienteServicio;
import pe.com.sikalma.service.PsicologoServicio;
import pe.com.sikalma.service.ServicioPsicologicoServicio;
import pe.com.sikalma.service.UsuarioDetallesServicio;
import pe.com.sikalma.service.UsuarioSistemaServicio;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SikalmaV3FuncionalidadesTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EspecialidadRepositorio especialidadRepositorio;

    @Autowired
    private ServicioRepositorio servicioRepositorio;

    @Autowired
    private PsicologoRepositorio psicologoRepositorio;

    @Autowired
    private PacienteRepositorio pacienteRepositorio;

    @Autowired
    private UsuarioSistemaRepositorio usuarioRepositorio;

    @Autowired
    private CitaRepositorio citaRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UsuarioSistemaServicio usuarioSistemaServicio;

    @Autowired
    private UsuarioDetallesServicio usuarioDetallesServicio;

    @Autowired
    private CitaServicio citaServicio;

    @Autowired
    private PacienteServicio pacienteServicio;

    @Autowired
    private PsicologoServicio psicologoServicio;

    @Autowired
    private ServicioPsicologicoServicio servicioPsicologicoServicio;

    @Autowired
    private DashboardMetricasServicio metricasServicio;

    private InicializadorDatosDemo crearInicializador() {
        return new InicializadorDatosDemo(
                especialidadRepositorio,
                servicioRepositorio,
                psicologoRepositorio,
                pacienteRepositorio,
                usuarioRepositorio,
                citaRepositorio,
                passwordEncoder,
                Clock.systemDefaultZone());
    }

    private void reiniciarYSembrarDatosDemo() {
        citaRepositorio.deleteAll();
        usuarioRepositorio.deleteAll();
        pacienteRepositorio.deleteAll();
        psicologoRepositorio.deleteAll();
        servicioRepositorio.deleteAll();
        especialidadRepositorio.deleteAll();
        entityManager.flush();

        crearInicializador().run(null);
        entityManager.flush();
    }

    @Test
    void inicializadorDatosDemoSiembraCatalogoYEsIdempotente() {
        reiniciarYSembrarDatosDemo();

        long usuariosAntes = usuarioRepositorio.count();
        long citasAntes = citaRepositorio.count();
        assertTrue(usuariosAntes >= 4);
        assertTrue(citasAntes >= 8);
        assertTrue(especialidadRepositorio.count() >= 4);
        assertTrue(servicioRepositorio.count() >= 5);
        assertTrue(psicologoRepositorio.count() >= 3);
        assertTrue(pacienteRepositorio.count() >= 5);

        // Segunda invocación no duplica registros existentes
        crearInicializador().inicializarSiVacio();
        assertEquals(usuariosAntes, usuarioRepositorio.count());
        assertEquals(citasAntes, citaRepositorio.count());
    }

    @Test
    void sitioPublicoYVideosEstaticosSonAccesiblesSinAutenticacion() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("sikalma-consultorio-hero.mp4")))
                .andExpect(content().string(containsString("sikalma-kids-vertical.mp4")));

        mockMvc.perform(get("/video/sikalma-consultorio-hero.mp4"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/video/sikalma-kids-vertical.mp4"))
                .andExpect(status().isOk());
    }

    @Test
    void loginPorCadaRolRedirigeYControlaBienvenidaYAislamientoRbac() throws Exception {
        reiniciarYSembrarDatosDemo();

        // 1. Administrador
        MvcResult loginAdmin = mockMvc.perform(formLogin().user("admin").password("Admin123!"))
                .andExpect(authenticated().withUsername("admin"))
                .andExpect(redirectedUrl("/admin/inicio"))
                .andReturn();
        MockHttpSession sesionAdmin = (MockHttpSession) loginAdmin.getRequest().getSession(false);
        assertNotNull(sesionAdmin);
        assertEquals(Boolean.TRUE, sesionAdmin.getAttribute(ManejadorIngresoExitoso.ATTR_BIENVENIDA_PENDIENTE));

        mockMvc.perform(get("/admin").session(sesionAdmin))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/inicio"));

        // Primera visita muestra overlay animado de bienvenida y consume la bandera de sesión
        mockMvc.perform(get("/admin/inicio").session(sesionAdmin))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id=\"welcome-overlay\"")));

        mockMvc.perform(get("/admin/inicio").session(sesionAdmin))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not(containsString("id=\"welcome-overlay\""))));

        // 2. Recepcionista
        MvcResult loginRecep = mockMvc.perform(formLogin().user("recepcion").password("Recepcion123!"))
                .andExpect(authenticated().withUsername("recepcion"))
                .andExpect(redirectedUrl("/recepcion/inicio"))
                .andReturn();
        MockHttpSession sesionRecep = (MockHttpSession) loginRecep.getRequest().getSession(false);

        mockMvc.perform(get("/recepcion/inicio").session(sesionRecep))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("welcome-overlay--RECEPCIONISTA")));

        mockMvc.perform(get("/recepcion/citas").session(sesionRecep)).andExpect(status().isOk());
        mockMvc.perform(get("/recepcion/pacientes").session(sesionRecep)).andExpect(status().isOk());
        mockMvc.perform(get("/admin/usuarios").session(sesionRecep)).andExpect(status().isForbidden());
        mockMvc.perform(get("/profesional/inicio").session(sesionRecep)).andExpect(status().isForbidden());

        // 3. Psicóloga (acceso exclusivo a sus propias citas)
        MvcResult loginPsico = mockMvc.perform(formLogin().user("maryory").password("Psico123!"))
                .andExpect(authenticated().withUsername("maryory"))
                .andExpect(redirectedUrl("/profesional/inicio"))
                .andReturn();
        MockHttpSession sesionPsico = (MockHttpSession) loginPsico.getRequest().getSession(false);

        mockMvc.perform(get("/profesional/inicio").session(sesionPsico))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("welcome-overlay--PSICOLOGO")));

        mockMvc.perform(get("/admin/inicio").session(sesionPsico)).andExpect(status().isForbidden());
        mockMvc.perform(get("/recepcion/citas").session(sesionPsico)).andExpect(status().isForbidden());

        // Verificar aislamiento cruzado entre profesionales
        Long idAntony = usuarioDetallesServicio.buscarPorUsername("antony").orElseThrow().getPsicologo().getId();
        Long citaDeAntony = citaServicio.listarParaProfesional(idAntony, null).get(0).getId();
        mockMvc.perform(get("/profesional/citas/" + citaDeAntony + "/atencion").session(sesionPsico))
                .andExpect(status().isForbidden());

        // 4. Fisioterapeuta
        MvcResult loginFisio = mockMvc.perform(formLogin().user("antony").password("Fisio123!"))
                .andExpect(authenticated().withUsername("antony"))
                .andExpect(redirectedUrl("/profesional/inicio"))
                .andReturn();
        MockHttpSession sesionFisio = (MockHttpSession) loginFisio.getRequest().getSession(false);
        mockMvc.perform(get("/profesional/inicio").session(sesionFisio))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("welcome-overlay--FISIOTERAPEUTA")));
    }

    @Test
    void gestionCompletaDeUsuariosYValidacionesDeAcceso() throws Exception {
        reiniciarYSembrarDatosDemo();

        assertTrue(usuarioDetallesServicio.buscarPorUsername(null).isEmpty());
        assertTrue(usuarioDetallesServicio.buscarPorUsername("   ").isEmpty());
        assertThrows(UsernameNotFoundException.class, () -> usuarioDetallesServicio.loadUserByUsername("no_existe"));

        // Alta sin contraseña o contraseña corta lanza validación de servicio
        UsuarioFormulario sinClave = new UsuarioFormulario();
        sinClave.setUsername("sinclave");
        sinClave.setNombreCompleto("Sin Clave");
        sinClave.setRol(RolUsuario.RECEPCIONISTA);
        assertThrows(RegistroDuplicadoException.class, () -> usuarioSistemaServicio.guardar(sinClave));

        sinClave.setPassword("123");
        assertThrows(RegistroDuplicadoException.class, () -> usuarioSistemaServicio.guardar(sinClave));

        // Duplicado de username y correo
        UsuarioFormulario dupUser = new UsuarioFormulario();
        dupUser.setUsername("ADMIN");
        dupUser.setNombreCompleto("Otro Admin");
        dupUser.setPassword("Clave1234");
        dupUser.setRol(RolUsuario.ADMINISTRADOR);
        assertThrows(RegistroDuplicadoException.class, () -> usuarioSistemaServicio.guardar(dupUser));

        UsuarioFormulario dupEmail = new UsuarioFormulario();
        dupEmail.setUsername("otro_user");
        dupEmail.setNombreCompleto("Otro Correo");
        dupEmail.setCorreo("ADMIN@SIKALMA.PE");
        dupEmail.setPassword("Clave1234");
        dupEmail.setRol(RolUsuario.ADMINISTRADOR);
        assertThrows(RegistroDuplicadoException.class, () -> usuarioSistemaServicio.guardar(dupEmail));

        // Creación válida vía controlador admin y verificación de inicio de sesión con nuevo usuario
        Long psiId = psicologoServicio.listar(null).get(0).getId();
        mockMvc.perform(post("/admin/usuarios/guardar")
                        .with(user("admin").roles("ADMINISTRADOR"))
                        .with(csrf())
                        .param("username", "nueva_recepcion")
                        .param("nombreCompleto", "Lucia Vargas")
                        .param("correo", "lucia.vargas@sikalma.pe")
                        .param("rol", "RECEPCIONISTA")
                        .param("psicologoId", String.valueOf(psiId))
                        .param("password", "NuevaClave123")
                        .param("activo", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/usuarios"));

        mockMvc.perform(formLogin().user("nueva_recepcion").password("NuevaClave123"))
                .andExpect(authenticated().withUsername("nueva_recepcion").withRoles("RECEPCIONISTA"))
                .andExpect(redirectedUrl("/recepcion/inicio"));

        // Edición sin cambiar contraseña conserva hash; luego alternar estado deshabilita login
        UsuarioSistema creada = usuarioDetallesServicio.buscarPorUsername("nueva_recepcion").orElseThrow();
        UsuarioFormulario edit = usuarioSistemaServicio.obtenerFormulario(creada.getId());
        edit.setNombreCompleto("Lucia Vargas Actualizada");
        edit.setPassword("");
        usuarioSistemaServicio.guardar(edit);

        mockMvc.perform(get("/admin/usuarios").with(user("admin").roles("ADMINISTRADOR")).param("buscar", "Lucia"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/admin/usuarios/nuevo").with(user("admin").roles("ADMINISTRADOR")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/admin/usuarios/" + creada.getId() + "/editar").with(user("admin").roles("ADMINISTRADOR")))
                .andExpect(status().isOk());
        mockMvc.perform(post("/admin/usuarios/guardar")
                        .with(user("admin").roles("ADMINISTRADOR"))
                        .with(csrf())
                        .param("username", "a"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/usuarios/" + creada.getId() + "/estado")
                        .with(user("admin").roles("ADMINISTRADOR"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/usuarios"));

        assertFalse(usuarioDetallesServicio.buscarPorUsername("nueva_recepcion").orElseThrow().isActivo());

        mockMvc.perform(formLogin().user("nueva_recepcion").password("NuevaClave123"))
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));

        assertThrows(RegistroNoEncontradoException.class, () -> usuarioSistemaServicio.obtenerFormulario(987654L));
    }

    @Test
    void gestionDeCitasDetectaCrucesHorariosSugiereAlternativasYCicloClinicoCompleto() throws Exception {
        reiniciarYSembrarDatosDemo();

        Paciente paciente = pacienteServicio.listar(null).get(0);
        Psicologo maryory = usuarioDetallesServicio.buscarPorUsername("maryory").orElseThrow().getPsicologo();
        Servicio servicio = servicioPsicologicoServicio.listarActivos().get(0);
        LocalDate fechaNueva = LocalDate.now().plusDays(15);

        // 1. Registrar primera cita válida vía controlador /recepcion/citas
        mockMvc.perform(post("/recepcion/citas/guardar")
                        .with(user("recepcion").roles("RECEPCIONISTA"))
                        .with(csrf())
                        .param("pacienteId", String.valueOf(paciente.getId()))
                        .param("psicologoId", String.valueOf(maryory.getId()))
                        .param("servicioId", String.valueOf(servicio.getId()))
                        .param("modalidad", "PRESENCIAL")
                        .param("fecha", fechaNueva.toString())
                        .param("horaInicio", "10:00")
                        .param("estado", "PENDIENTE")
                        .param("motivoConsulta", "Evaluación integral"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recepcion/citas"));

        List<Cita> filtradas = citaServicio.listar(paciente.getNumeroDocumento(), EstadoCita.PENDIENTE, maryory.getId());
        assertFalse(filtradas.isEmpty());
        Cita creada = filtradas.stream().filter(c -> fechaNueva.equals(c.getFecha())).findFirst().orElseThrow();

        // 2. Intentar registrar en horario traslapado (10:20) dispara ConflictoHorarioException con sugerencias
        CitaFormulario traslapada = new CitaFormulario();
        traslapada.setPacienteId(paciente.getId());
        traslapada.setPsicologoId(maryory.getId());
        traslapada.setServicioId(servicio.getId());
        traslapada.setFecha(fechaNueva);
        traslapada.setHoraInicio(LocalTime.of(10, 20));
        traslapada.setModalidad(ModalidadAtencion.VIRTUAL);
        traslapada.setEstado(EstadoCita.CONFIRMADA);

        ConflictoHorarioException conflicto = assertThrows(
                ConflictoHorarioException.class,
                () -> citaServicio.guardar(traslapada));
        assertFalse(conflicto.getHorariosAlternativos().isEmpty());

        // Controlador muestra el cuadro de conflicto sin perder el formulario
        mockMvc.perform(post("/admin/citas/guardar")
                        .with(user("admin").roles("ADMINISTRADOR"))
                        .with(csrf())
                        .param("pacienteId", String.valueOf(paciente.getId()))
                        .param("psicologoId", String.valueOf(maryory.getId()))
                        .param("servicioId", String.valueOf(servicio.getId()))
                        .param("modalidad", "VIRTUAL")
                        .param("fecha", fechaNueva.toString())
                        .param("horaInicio", "10:20")
                        .param("estado", "CONFIRMADA"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Horarios libres sugeridos")));

        // Cita CANCELADA no genera bloqueo y edición propia no se auto-bloquea
        traslapada.setEstado(EstadoCita.CANCELADA);
        citaServicio.guardar(traslapada);

        CitaFormulario edicionPropia = citaServicio.obtenerFormulario(creada.getId());
        edicionPropia.setMotivoConsulta("Motivo actualizado");
        citaServicio.guardar(edicionPropia);

        // Listados, comprobante y flujo de estado + atención clínica del profesional
        mockMvc.perform(get("/admin/citas").with(user("admin").roles("ADMINISTRADOR"))
                        .param("buscar", maryory.getNombres()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/admin/citas/nuevo").with(user("admin").roles("ADMINISTRADOR"))).andExpect(status().isOk());
        mockMvc.perform(get("/admin/citas/" + creada.getId() + "/editar").with(user("admin").roles("ADMINISTRADOR")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/admin/citas/" + creada.getId() + "/comprobante").with(user("admin").roles("ADMINISTRADOR")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Comprobante de Cita")));

        mockMvc.perform(post("/recepcion/citas/" + creada.getId() + "/estado")
                        .with(user("recepcion").roles("RECEPCIONISTA"))
                        .with(csrf())
                        .param("estado", "EN_SALA"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recepcion/citas"));

        // Psicóloga accede a su cita, valida error si nota vacía, luego guarda evolución
        mockMvc.perform(get("/profesional/inicio").with(user("maryory").roles("PSICOLOGO"))
                        .param("estado", "EN_SALA"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/profesional/citas/" + creada.getId() + "/atencion").with(user("maryory").roles("PSICOLOGO")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/profesional/citas/" + creada.getId() + "/atencion")
                        .with(user("maryory").roles("PSICOLOGO"))
                        .with(csrf())
                        .param("citaId", String.valueOf(creada.getId()))
                        .param("estado", "ATENDIDA")
                        .param("notaEvolucion", ""))
                .andExpect(status().isOk());

        mockMvc.perform(post("/profesional/citas/" + creada.getId() + "/atencion")
                        .with(user("maryory").roles("PSICOLOGO"))
                        .with(csrf())
                        .param("citaId", String.valueOf(creada.getId()))
                        .param("estado", "ATENDIDA")
                        .param("diagnosticoReferencial", "Regulación emocional favorable")
                        .param("notaEvolucion", "Sesión completada con ejercicios de respiración diafragmática."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profesional/inicio"));

        NotaEvolucionFormulario recargada = citaServicio.obtenerFormularioNota(creada.getId());
        assertEquals("Regulación emocional favorable", recargada.getDiagnosticoReferencial());

        // Flujo de recepción para crear y guardar paciente
        mockMvc.perform(get("/recepcion/pacientes/nuevo").with(user("recepcion").roles("RECEPCIONISTA")))
                .andExpect(status().isOk());
        mockMvc.perform(post("/recepcion/pacientes/guardar")
                        .with(user("recepcion").roles("RECEPCIONISTA"))
                        .with(csrf())
                        .param("tipoDocumento", "DNI")
                        .param("numeroDocumento", "76543219")
                        .param("nombres", "Camila")
                        .param("apellidos", "Salazar")
                        .param("fechaNacimiento", "2002-09-12")
                        .param("telefono", "964123456")
                        .param("consentimientoRegistrado", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recepcion/pacientes"));

        // Errores cuando faltan referencias
        CitaFormulario faltante = new CitaFormulario();
        faltante.setPacienteId(999999L);
        faltante.setPsicologoId(maryory.getId());
        faltante.setServicioId(servicio.getId());
        faltante.setFecha(fechaNueva);
        faltante.setHoraInicio(LocalTime.of(12, 0));
        assertThrows(RegistroNoEncontradoException.class, () -> citaServicio.guardar(faltante));

        assertThrows(RegistroNoEncontradoException.class, () -> citaServicio.obtenerPorId(999999L));
    }

    @Test
    void metricasDashboardYEnumeracionesV3FuncionanEnVacioYConDatos() {
        citaRepositorio.deleteAll();
        ResumenMetricasDto sinCitas = metricasServicio.calcularMetricas();
        assertEquals(0, sinCitas.totalCitas());
        assertEquals(0, sinCitas.tasaAsistenciaPorcentaje());

        reiniciarYSembrarDatosDemo();
        ResumenMetricasDto conDatos = metricasServicio.calcularMetricas();
        assertTrue(conDatos.totalCitas() >= 8);
        assertEquals(5, conDatos.citasPorEstado().size());
        assertFalse(conDatos.citasPorServicio().isEmpty());
        assertEquals(2, conDatos.citasPorModalidad().size());
        assertTrue(conDatos.tasaAsistenciaPorcentaje() > 0);

        for (RolUsuario rol : RolUsuario.values()) {
            assertNotNull(rol.getEtiqueta());
            assertNotNull(rol.getRutaInicio());
            assertNotNull(rol.getDescripcionBienvenida());
        }
        for (EstadoCita estado : EstadoCita.values()) {
            assertNotNull(estado.getEtiqueta());
            assertNotNull(estado.getClaseCss());
        }
    }
}
