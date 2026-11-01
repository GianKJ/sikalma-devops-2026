package pe.com.sikalma;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;

import pe.com.sikalma.dto.EspecialidadFormulario;
import pe.com.sikalma.dto.PacienteFormulario;
import pe.com.sikalma.dto.PsicologoFormulario;
import pe.com.sikalma.dto.ServicioFormulario;
import pe.com.sikalma.entity.Especialidad;
import pe.com.sikalma.entity.ModalidadAtencion;
import pe.com.sikalma.entity.Paciente;
import pe.com.sikalma.entity.Psicologo;
import pe.com.sikalma.entity.Servicio;
import pe.com.sikalma.entity.TipoDocumento;
import pe.com.sikalma.exception.RegistroDuplicadoException;
import pe.com.sikalma.exception.RegistroNoEncontradoException;
import pe.com.sikalma.service.EspecialidadServicio;
import pe.com.sikalma.service.PacienteServicio;
import pe.com.sikalma.service.PsicologoServicio;
import pe.com.sikalma.service.ServicioPsicologicoServicio;
import pe.com.sikalma.service.TextoNormalizadorUtil;
import pe.com.sikalma.validation.ValidacionDocumento;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "admin", roles = "ADMINISTRADOR")
class ServiciosYControladoresV2Tests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PacienteServicio pacienteServicio;

    @Autowired
    private PsicologoServicio psicologoServicio;

    @Autowired
    private EspecialidadServicio especialidadServicio;

    @Autowired
    private ServicioPsicologicoServicio servicioPsicologicoServicio;

    @BeforeEach
    void inicializarDatosBase() {
        EspecialidadFormulario ef = new EspecialidadFormulario();
        ef.setNombre("Psicología Base");
        ef.setDescripcion("Base");
        especialidadServicio.guardar(ef);

        ServicioFormulario sf = new ServicioFormulario();
        sf.setNombre("Servicio Base");
        sf.setDescripcion("Base");
        sf.setModalidad(ModalidadAtencion.AMBAS);
        sf.setDuracionMinutos(45);
        sf.setPublicoObjetivo("General");
        servicioPsicologicoServicio.guardar(sf);

        PacienteFormulario pac = new PacienteFormulario();
        pac.setTipoDocumento(TipoDocumento.DNI);
        pac.setNumeroDocumento("70000099");
        pac.setNombres("Base");
        pac.setApellidos("Paciente");
        pac.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        pac.setTelefono("900000099");
        pac.setCorreo("base.paciente@sikalma.test");
        pac.setConsentimientoRegistrado(true);
        pacienteServicio.guardar(pac);

        PsicologoFormulario psi = new PsicologoFormulario();
        psi.setTipoDocumento(TipoDocumento.DNI);
        psi.setNumeroDocumento("70000098");
        psi.setNombres("Base");
        psi.setApellidos("Psicologo");
        psi.setColegiatura("CPSP-0098");
        psi.setTelefono("900000098");
        psi.setCorreo("base.psicologo@sikalma.test");
        psi.setModalidad(ModalidadAtencion.AMBAS);
        psicologoServicio.guardar(psi);
    }

    @Test
    void normalizadorTextoConvierteNulosYEspacios() {
        assertNull(TextoNormalizadorUtil.opcional(null));
        assertNull(TextoNormalizadorUtil.opcional("   "));
        assertEquals("Hola", TextoNormalizadorUtil.opcional("  Hola  "));
        assertNull(TextoNormalizadorUtil.opcionalMinuscula(""));
        assertEquals("correo@sikalma.pe", TextoNormalizadorUtil.opcionalMinuscula("  CORREO@SIKALMA.PE "));
        assertNull(TextoNormalizadorUtil.opcionalMayuscula(null));
        assertEquals("CPSP-100", TextoNormalizadorUtil.opcionalMayuscula(" cpsp-100 "));
    }

    @Test
    void validacionDocumentoRechazaDniInvalidoYAceptaCarnetExtranjeria() {
        PacienteFormulario form = new PacienteFormulario();
        BeanPropertyBindingResult erroresDni = new BeanPropertyBindingResult(form, "formulario");
        ValidacionDocumento.validarDni(TipoDocumento.DNI, "1234", erroresDni);
        assertTrue(erroresDni.hasFieldErrors("numeroDocumento"));

        BeanPropertyBindingResult erroresNulo = new BeanPropertyBindingResult(form, "formulario");
        ValidacionDocumento.validarDni(TipoDocumento.DNI, null, erroresNulo);
        assertTrue(erroresNulo.hasFieldErrors("numeroDocumento"));

        BeanPropertyBindingResult erroresCe = new BeanPropertyBindingResult(form, "formulario");
        ValidacionDocumento.validarDni(TipoDocumento.CARNET_EXTRANJERIA, "CE-998877", erroresCe);
        assertFalse(erroresCe.hasErrors());
    }

    @Test
    void pacienteServicioCicloCompletoBusquedaEdicionYEstado() {
        PacienteFormulario f = new PacienteFormulario();
        f.setTipoDocumento(TipoDocumento.DNI);
        f.setNumeroDocumento("71234567");
        f.setNombres("Lucia");
        f.setApellidos("Mendoza");
        f.setFechaNacimiento(LocalDate.of(1995, 5, 10));
        f.setTelefono("987654321");
        f.setCorreo("lucia@sikalma.test");
        f.setDireccion("Av. Principal 123");
        f.setContactoEmergencia("Carlos 999888777");
        f.setApoderado("");
        f.setObservacionesAdministrativas("Primera consulta");
        f.setConsentimientoRegistrado(true);
        f.setPreferenciasPrivacidad("WhatsApp");
        pacienteServicio.guardar(f);

        List<Paciente> filtrados = pacienteServicio.listar("71234567");
        assertEquals(1, filtrados.size());
        Long id = filtrados.get(0).getId();

        PacienteFormulario cargado = pacienteServicio.obtenerFormulario(id);
        assertEquals("Lucia", cargado.getNombres());
        assertNotNull(filtrados.get(0).getFechaCreacion());
        assertNotNull(filtrados.get(0).getFechaActualizacion());

        cargado.setNombres("Lucia Maria");
        pacienteServicio.guardar(cargado);

        pacienteServicio.alternarEstado(id);
        assertFalse(pacienteServicio.listar("71234567").get(0).isActivo());
    }

    @Test
    void pacienteServicioLanzaExcepcionPorDniOCorreoDuplicado() {
        PacienteFormulario f1 = new PacienteFormulario();
        f1.setTipoDocumento(TipoDocumento.DNI);
        f1.setNumeroDocumento("72223334");
        f1.setNombres("Pedro");
        f1.setApellidos("Rojas");
        f1.setFechaNacimiento(LocalDate.of(1990, 1, 15));
        f1.setTelefono("911222333");
        f1.setCorreo("pedro@sikalma.test");
        f1.setConsentimientoRegistrado(true);
        pacienteServicio.guardar(f1);

        PacienteFormulario fDuplicadoDni = new PacienteFormulario();
        fDuplicadoDni.setTipoDocumento(TipoDocumento.DNI);
        fDuplicadoDni.setNumeroDocumento("72223334");
        fDuplicadoDni.setNombres("Otro");
        fDuplicadoDni.setApellidos("Paciente");
        fDuplicadoDni.setFechaNacimiento(LocalDate.of(1992, 2, 2));
        fDuplicadoDni.setTelefono("944555666");
        fDuplicadoDni.setConsentimientoRegistrado(true);

        RegistroDuplicadoException exDni = assertThrows(
                RegistroDuplicadoException.class,
                () -> pacienteServicio.guardar(fDuplicadoDni));
        assertEquals("numeroDocumento", exDni.getCampo());

        PacienteFormulario fDuplicadoCorreo = new PacienteFormulario();
        fDuplicadoCorreo.setTipoDocumento(TipoDocumento.DNI);
        fDuplicadoCorreo.setNumeroDocumento("73334445");
        fDuplicadoCorreo.setNombres("Otro");
        fDuplicadoCorreo.setApellidos("Correo");
        fDuplicadoCorreo.setFechaNacimiento(LocalDate.of(1992, 2, 2));
        fDuplicadoCorreo.setTelefono("944555666");
        fDuplicadoCorreo.setCorreo("PEDRO@sikalma.test");
        fDuplicadoCorreo.setConsentimientoRegistrado(true);

        assertThrows(RegistroDuplicadoException.class, () -> pacienteServicio.guardar(fDuplicadoCorreo));
    }

    @Test
    void pacienteServicioLanzaExcepcionCuandoIdNoExiste() {
        assertThrows(RegistroNoEncontradoException.class, () -> pacienteServicio.obtenerFormulario(999999L));
    }

    @Test
    void especialidadServicioCicloCompletoDuplicadosYNoEncontrado() {
        EspecialidadFormulario ef = new EspecialidadFormulario();
        ef.setNombre("Neuropsicología Infantil");
        ef.setDescripcion("Evaluación cognitiva");
        especialidadServicio.guardar(ef);

        List<Especialidad> lista = especialidadServicio.listar("Neuropsicología");
        assertEquals(1, lista.size());
        Long id = lista.get(0).getId();

        EspecialidadFormulario editForm = especialidadServicio.obtenerFormulario(id);
        editForm.setDescripcion("Evaluación actualizada");
        especialidadServicio.guardar(editForm);

        especialidadServicio.alternarEstado(id);

        EspecialidadFormulario dup = new EspecialidadFormulario();
        dup.setNombre("neuropsicología infantil");
        assertThrows(RegistroDuplicadoException.class, () -> especialidadServicio.guardar(dup));
        assertThrows(RegistroNoEncontradoException.class, () -> especialidadServicio.obtenerFormulario(888888L));
    }

    @Test
    void servicioPsicologicoCicloCompletoDuplicadosYNoEncontrado() {
        ServicioFormulario sf = new ServicioFormulario();
        sf.setNombre("Terapia de Pareja Avanzada");
        sf.setDescripcion("Sesiones conjuntas");
        sf.setModalidad(ModalidadAtencion.PRESENCIAL);
        sf.setDuracionMinutos(60);
        sf.setPublicoObjetivo("Parejas");
        servicioPsicologicoServicio.guardar(sf);

        List<Servicio> encontrados = servicioPsicologicoServicio.listar("Pareja Avanzada");
        assertEquals(1, encontrados.size());
        Long id = encontrados.get(0).getId();

        ServicioFormulario edit = servicioPsicologicoServicio.obtenerFormulario(id);
        edit.setDuracionMinutos(75);
        servicioPsicologicoServicio.guardar(edit);

        servicioPsicologicoServicio.alternarEstado(id);

        ServicioFormulario dup = new ServicioFormulario();
        dup.setNombre("terapia de pareja avanzada");
        dup.setDescripcion("Otra");
        dup.setModalidad(ModalidadAtencion.VIRTUAL);
        dup.setDuracionMinutos(45);
        dup.setPublicoObjetivo("Adultos");
        assertThrows(RegistroDuplicadoException.class, () -> servicioPsicologicoServicio.guardar(dup));
        assertThrows(RegistroNoEncontradoException.class, () -> servicioPsicologicoServicio.obtenerFormulario(777777L));
    }

    @Test
    void psicologoServicioCicloCompletoRelacionesDuplicadosYNoEncontrado() {
        Long espId = especialidadServicio.listar(null).get(0).getId();
        Long srvId = servicioPsicologicoServicio.listar(null).get(0).getId();

        PsicologoFormulario pf = new PsicologoFormulario();
        pf.setTipoDocumento(TipoDocumento.DNI);
        pf.setNumeroDocumento("74445556");
        pf.setNombres("Elena");
        pf.setApellidos("Valdivia");
        pf.setColegiatura("CPSP-7444");
        pf.setTelefono("955666777");
        pf.setCorreo("elena@sikalma.test");
        pf.setDescripcionProfesional("Especialista clínica");
        pf.setModalidad(ModalidadAtencion.AMBAS);
        pf.setEspecialidadIds(Set.of(espId));
        pf.setServicioIds(Set.of(srvId));
        psicologoServicio.guardar(pf);

        List<Psicologo> lista = psicologoServicio.listar("74445556");
        assertEquals(1, lista.size());
        Long id = lista.get(0).getId();

        PsicologoFormulario cargado = psicologoServicio.obtenerFormulario(id);
        assertTrue(cargado.getEspecialidadIds().contains(espId));
        assertTrue(cargado.getServicioIds().contains(srvId));

        cargado.setNombres("Elena Sofia");
        psicologoServicio.guardar(cargado);
        psicologoServicio.alternarEstado(id);

        PsicologoFormulario dupCol = new PsicologoFormulario();
        dupCol.setTipoDocumento(TipoDocumento.DNI);
        dupCol.setNumeroDocumento("75556667");
        dupCol.setNombres("Marco");
        dupCol.setApellidos("Polo");
        dupCol.setColegiatura("cpsp-7444");
        dupCol.setTelefono("955666000");
        dupCol.setModalidad(ModalidadAtencion.VIRTUAL);
        assertThrows(RegistroDuplicadoException.class, () -> psicologoServicio.guardar(dupCol));
        assertThrows(RegistroNoEncontradoException.class, () -> psicologoServicio.obtenerFormulario(666666L));
    }

    @Test
    void controladorPacientesManejaBusquedaEdicionErroresYEstado() throws Exception {
        Long id = pacienteServicio.listar(null).get(0).getId();
        String dniExistente = pacienteServicio.listar(null).get(0).getNumeroDocumento();

        mockMvc.perform(get("/admin/pacientes").param("buscar", dniExistente))
                .andExpect(status().isOk());

        mockMvc.perform(get("/admin/pacientes/" + id + "/editar"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/pacientes/guardar")
                        .with(csrf())
                        .param("tipoDocumento", "DNI")
                        .param("numeroDocumento", "123")
                        .param("nombres", "")
                        .param("apellidos", ""))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/pacientes/guardar")
                        .with(csrf())
                        .param("tipoDocumento", "DNI")
                        .param("numeroDocumento", dniExistente)
                        .param("nombres", "Duplicado")
                        .param("apellidos", "Test")
                        .param("fechaNacimiento", "1990-05-05")
                        .param("telefono", "999888777")
                        .param("consentimientoRegistrado", "true"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/pacientes/" + id + "/estado").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/pacientes"));
    }

    @Test
    void controladorPsicologosManejaBusquedaEdicionErroresYEstado() throws Exception {
        Long id = psicologoServicio.listar(null).get(0).getId();
        String dniExistente = psicologoServicio.listar(null).get(0).getNumeroDocumento();

        mockMvc.perform(get("/admin/psicologos").param("buscar", dniExistente))
                .andExpect(status().isOk());

        mockMvc.perform(get("/admin/psicologos/" + id + "/editar"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/psicologos/guardar")
                        .with(csrf())
                        .param("tipoDocumento", "DNI")
                        .param("numeroDocumento", "12"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/psicologos/guardar")
                        .with(csrf())
                        .param("tipoDocumento", "DNI")
                        .param("numeroDocumento", dniExistente)
                        .param("nombres", "Duplicado")
                        .param("apellidos", "Psicologo")
                        .param("colegiatura", "CPSP-99999")
                        .param("telefono", "999111222")
                        .param("modalidad", "AMBAS"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/psicologos/" + id + "/estado").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/psicologos"));
    }

    @Test
    void controladorEspecialidadesManejaBusquedaEdicionErroresYEstado() throws Exception {
        Long id = especialidadServicio.listar(null).get(0).getId();
        String nombreExistente = especialidadServicio.listar(null).get(0).getNombre();

        mockMvc.perform(get("/admin/especialidades").param("buscar", nombreExistente))
                .andExpect(status().isOk());

        mockMvc.perform(get("/admin/especialidades/" + id + "/editar"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/especialidades/guardar")
                        .with(csrf())
                        .param("nombre", ""))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/especialidades/guardar")
                        .with(csrf())
                        .param("nombre", nombreExistente)
                        .param("descripcion", "Duplicada"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/especialidades/" + id + "/estado").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/especialidades"));
    }

    @Test
    void controladorServiciosManejaBusquedaEdicionErroresYEstado() throws Exception {
        Long id = servicioPsicologicoServicio.listar(null).get(0).getId();
        String nombreExistente = servicioPsicologicoServicio.listar(null).get(0).getNombre();

        mockMvc.perform(get("/admin/servicios").param("buscar", nombreExistente))
                .andExpect(status().isOk());

        mockMvc.perform(get("/admin/servicios/" + id + "/editar"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/servicios/guardar")
                        .with(csrf())
                        .param("nombre", ""))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/servicios/guardar")
                        .with(csrf())
                        .param("nombre", nombreExistente)
                        .param("descripcion", "Desc")
                        .param("modalidad", "AMBAS")
                        .param("duracionMinutos", "50")
                        .param("publicoObjetivo", "Todos"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/servicios/" + id + "/estado").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/servicios"));
    }

    @Test
    void verificaEtiquetasDeEnumeracionesYPropiedades() {
        for (ModalidadAtencion m : ModalidadAtencion.values()) {
            assertNotNull(m.getEtiqueta());
        }
        for (TipoDocumento t : TipoDocumento.values()) {
            assertNotNull(t.getEtiqueta());
        }
    }

    @Test
    void verificaRutasPublicasAdicionales() throws Exception {
        mockMvc.perform(get("/inicio")).andExpect(status().isOk());
        mockMvc.perform(get("/acceso-denegado")).andExpect(status().isOk());
    }

    @Test
    void psicologoServicioDetectaCorreoDuplicado() {
        Psicologo existente = psicologoServicio.listar(null).get(0);
        PsicologoFormulario f = new PsicologoFormulario();
        f.setTipoDocumento(TipoDocumento.DNI);
        f.setNumeroDocumento("79998887");
        f.setNombres("Test");
        f.setApellidos("CorreoDup");
        f.setColegiatura("CPSP-88887");
        f.setTelefono("999000111");
        f.setCorreo(existente.getCorreo());
        f.setModalidad(ModalidadAtencion.VIRTUAL);

        assertThrows(RegistroDuplicadoException.class, () -> psicologoServicio.guardar(f));
    }

    @Test
    void pacienteServicioPermiteGuardarConCorreoVacio() {
        PacienteFormulario f = new PacienteFormulario();
        f.setTipoDocumento(TipoDocumento.CARNET_EXTRANJERIA);
        f.setNumeroDocumento("CE-102030");
        f.setNombres("Extranjero");
        f.setApellidos("SinCorreo");
        f.setFechaNacimiento(LocalDate.of(1988, 8, 8));
        f.setTelefono("988777666");
        f.setCorreo("   ");
        f.setConsentimientoRegistrado(false);
        pacienteServicio.guardar(f);

        assertEquals(1, pacienteServicio.listar("CE-102030").size());
    }

    @Test
    void psicologoServicioPermiteGuardarSinCorreoNiColegiatura() {
        PsicologoFormulario f = new PsicologoFormulario();
        f.setTipoDocumento(TipoDocumento.CARNET_EXTRANJERIA);
        f.setNumeroDocumento("CE-506070");
        f.setNombres("Consultor");
        f.setApellidos("Externo");
        f.setColegiatura("  ");
        f.setCorreo("");
        f.setTelefono("977666555");
        f.setModalidad(ModalidadAtencion.PRESENCIAL);
        psicologoServicio.guardar(f);

        assertEquals(1, psicologoServicio.listar("CE-506070").size());
    }
}
