package pe.com.sikalma.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pe.com.sikalma.entity.Cita;
import pe.com.sikalma.entity.Especialidad;
import pe.com.sikalma.entity.EstadoCita;
import pe.com.sikalma.entity.ModalidadAtencion;
import pe.com.sikalma.entity.Paciente;
import pe.com.sikalma.entity.Psicologo;
import pe.com.sikalma.entity.RolUsuario;
import pe.com.sikalma.entity.Servicio;
import pe.com.sikalma.entity.TipoDocumento;
import pe.com.sikalma.entity.UsuarioSistema;
import pe.com.sikalma.repository.CitaRepositorio;
import pe.com.sikalma.repository.EspecialidadRepositorio;
import pe.com.sikalma.repository.PacienteRepositorio;
import pe.com.sikalma.repository.PsicologoRepositorio;
import pe.com.sikalma.repository.ServicioRepositorio;
import pe.com.sikalma.repository.UsuarioSistemaRepositorio;

@Component
@Profile("!test")
public class InicializadorDatosDemo implements ApplicationRunner {

    private final EspecialidadRepositorio especialidadRepositorio;
    private final ServicioRepositorio servicioRepositorio;
    private final PsicologoRepositorio psicologoRepositorio;
    private final PacienteRepositorio pacienteRepositorio;
    private final UsuarioSistemaRepositorio usuarioRepositorio;
    private final CitaRepositorio citaRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final Clock reloj;

    public InicializadorDatosDemo(
            EspecialidadRepositorio especialidadRepositorio,
            ServicioRepositorio servicioRepositorio,
            PsicologoRepositorio psicologoRepositorio,
            PacienteRepositorio pacienteRepositorio,
            UsuarioSistemaRepositorio usuarioRepositorio,
            CitaRepositorio citaRepositorio,
            PasswordEncoder passwordEncoder,
            Clock reloj) {
        this.especialidadRepositorio = especialidadRepositorio;
        this.servicioRepositorio = servicioRepositorio;
        this.psicologoRepositorio = psicologoRepositorio;
        this.pacienteRepositorio = pacienteRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.citaRepositorio = citaRepositorio;
        this.passwordEncoder = passwordEncoder;
        this.reloj = reloj;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        inicializarSiVacio();
    }

    @Transactional
    public void inicializarSiVacio() {
        if (usuarioRepositorio.count() > 0) {
            return;
        }

        Especialidad espClinica = crearEspecialidad("Psicología Clínica",
                "Evaluación, psicoterapia cognitivo conductual y manejo emocional en jóvenes y adultos.");
        Especialidad espInfantil = crearEspecialidad("Psicología Infantil y Estimulación",
                "Atención lúdica terapéutica, lenguaje y desarrollo integral en niños (SIKALMA KIDS).");
        Especialidad espFamiliar = crearEspecialidad("Terapia de Pareja y Familia",
                "Abordaje sistémico, comunicación asertiva y fortalecimiento de vínculos familiares.");
        Especialidad espFisio = crearEspecialidad("Terapia Física y Rehabilitación",
                "Recuperación funcional del movimiento, alivio del dolor y terapia ocupacional.");

        Servicio srvIndividual = crearServicio("Terapia Individual",
                "Acompañamiento psicológico personalizado para manejo de ansiedad, estrés y crecimiento personal.",
                ModalidadAtencion.AMBAS, 50, "Adolescentes, jóvenes y adultos");
        Servicio srvInfantil = crearServicio("Terapia Infantil y Estimulación",
                "Sesiones preparadas con cariño en SIKALMA KIDS para potenciar juego, lenguaje y desarrollo.",
                ModalidadAtencion.PRESENCIAL, 50, "Niños y primera infancia");
        Servicio srvPareja = crearServicio("Terapia de Pareja y Familiar",
                "Espacio seguro para mejorar la comunicación, resolver conflictos y reconstruir acuerdos.",
                ModalidadAtencion.AMBAS, 60, "Parejas y familias");
        Servicio srvVocacional = crearServicio("Orientación Vocacional",
                "Evaluación de aptitudes e intereses para elegir un proyecto académico y profesional sólido.",
                ModalidadAtencion.VIRTUAL, 60, "Adolescentes y jóvenes");
        Servicio srvFisio = crearServicio("Terapia Física y Rehabilitación",
                "Sesiones presenciales de rehabilitación motora y alivio muscular guiado.",
                ModalidadAtencion.PRESENCIAL, 50, "Adolescentes y adultos");

        Psicologo psiMaryory = basePsicologo("46210985", "Maryory Viviana", "Sánchez Vílchez",
                "C.Ps.P. 61905", ModalidadAtencion.AMBAS);
        psiMaryory = guardarPsicologo(psiMaryory, "993668057", "maryory.sanchez@sikalma.pe",
                "Psicóloga Clínica y CEO de SIKALMA. Especialista en terapia de parejas, psicoterapia sistémica, TREC, TCC y duelo.",
                Set.of(espClinica, espFamiliar), Set.of(srvIndividual, srvPareja, srvVocacional));

        Psicologo psiJudith = basePsicologo("47851209", "Judith", "Campos Pomachagua",
                "C.Ps.P. 41280", ModalidadAtencion.PRESENCIAL);
        psiJudith = guardarPsicologo(psiJudith, "962418502", "judith.campos@sikalma.pe",
                "Psicóloga especialista en TEA, análisis aplicado de conducta y estimulación en SIKALMA KIDS.",
                Set.of(espInfantil), Set.of(srvInfantil));

        Psicologo psiAntony = basePsicologo("71029384", "Antony", "Pérez Fernández",
                "C.T.M.P. 14890", ModalidadAtencion.PRESENCIAL);
        psiAntony = guardarPsicologo(psiAntony, "984210945", "antony.perez@sikalma.pe",
                "Fisioterapeuta especialista en terapia física, rehabilitación integral y terapia ocupacional.",
                Set.of(espFisio), Set.of(srvFisio));

        crearUsuario("admin", "Admin123!", "Giancarlo Sánchez — Dirección",
                "admin@sikalma.pe", RolUsuario.ADMINISTRADOR, null);
        crearUsuario("recepcion", "Recepcion123!", "Valeria Torres — Recepción",
                "recepcion@sikalma.pe", RolUsuario.RECEPCIONISTA, null);
        crearUsuario("maryory", "Psico123!", "Ps. Maryory V. Sánchez Vílchez",
                "maryory@sikalma.pe", RolUsuario.PSICOLOGO, psiMaryory);
        crearUsuario("antony", "Fisio123!", "Lic. Antony Pérez Fernández",
                "antony@sikalma.pe", RolUsuario.FISIOTERAPEUTA, psiAntony);

        Paciente pac1 = guardarPaciente(basePaciente("74125896", "Lucía Andrea", "Mendoza Carrillo", LocalDate.of(1998, 5, 14)),
                "951478230", "lucia.mendoza@correo.pe", null, true);
        Paciente pac2 = guardarPaciente(basePaciente("76543210", "Mateo Sebastián", "Huamán Quispe", LocalDate.of(2019, 8, 22)),
                "964201478", "padres.mateo@correo.pe", "Carla Quispe (Madre)", true);
        Paciente pac3 = guardarPaciente(basePaciente("43218765", "Carlos Eduardo", "Rojas Paredes", LocalDate.of(1991, 11, 3)),
                "945112233", "carlos.rojas@correo.pe", null, true);
        Paciente pac4 = guardarPaciente(basePaciente("75918273", "Sofía Camila", "Vega Morales", LocalDate.of(2005, 2, 19)),
                "987650123", "sofia.vega@correo.pe", "Luis Vega (Padre)", true);
        Paciente pac5 = guardarPaciente(basePaciente("41928374", "Elena Patricia", "Flores Guzmán", LocalDate.of(1987, 9, 10)),
                "991234876", "elena.flores@correo.pe", null, false);

        LocalDate hoy = LocalDate.now(reloj);
        List<Cita> citasDemo = List.of(
                conNota(baseCita(pac1, psiMaryory, srvIndividual, hoy, LocalTime.of(9, 0)),
                        ModalidadAtencion.PRESENCIAL, EstadoCita.ATENDIDA,
                        "Seguimiento por manejo de estrés laboral.",
                        "Paciente refiere mejoría en higiene del sueño y aplicación de técnicas de reestructuración cognitiva.",
                        "Ansiedad leve controlada"),
                conNota(baseCita(pac2, psiJudith, srvInfantil, hoy, LocalTime.of(10, 30)),
                        ModalidadAtencion.PRESENCIAL, EstadoCita.EN_SALA,
                        "Sesión de estimulación temprana y lenguaje expresivo en SIKALMA KIDS.",
                        null, null),
                conNota(baseCita(pac3, psiMaryory, srvPareja, hoy, LocalTime.of(15, 0)),
                        ModalidadAtencion.VIRTUAL, EstadoCita.CONFIRMADA,
                        "Acuerdos de comunicación asertiva en pareja.",
                        null, null),
                conNota(baseCita(pac4, psiMaryory, srvVocacional, hoy, LocalTime.of(17, 0)),
                        ModalidadAtencion.VIRTUAL, EstadoCita.PENDIENTE,
                        "Primera entrevista de orientación vocacional.",
                        null, null),
                conNota(baseCita(pac5, psiAntony, srvFisio, hoy.plusDays(1), LocalTime.of(11, 0)),
                        ModalidadAtencion.PRESENCIAL, EstadoCita.CONFIRMADA,
                        "Evaluación postural cervical.",
                        null, null),
                conNota(baseCita(pac1, psiMaryory, srvIndividual, hoy.minusDays(2), LocalTime.of(16, 0)),
                        ModalidadAtencion.VIRTUAL, EstadoCita.ATENDIDA,
                        "Control quincenal emocional.",
                        "Se consolidan estrategias de autorregulación y plan semanal de autocuidado.",
                        "Evolución favorable"),
                conNota(baseCita(pac2, psiJudith, srvInfantil, hoy.minusDays(1), LocalTime.of(9, 30)),
                        ModalidadAtencion.PRESENCIAL, EstadoCita.ATENDIDA,
                        "Refuerzo de motricidad fina y juego simbólico.",
                        "Buena respuesta a actividades de clasificación por colores y turnos.",
                        "Desarrollo acorde a edad"),
                conNota(baseCita(pac3, psiAntony, srvFisio, hoy.minusDays(3), LocalTime.of(18, 0)),
                        ModalidadAtencion.PRESENCIAL, EstadoCita.CANCELADA,
                        "Reprogramación solicitada por viaje laboral.",
                        null, null));
        citaRepositorio.saveAll(citasDemo);
    }

    private Especialidad crearEspecialidad(String nombre, String descripcion) {
        Especialidad e = new Especialidad();
        e.setNombre(nombre);
        e.setDescripcion(descripcion);
        return especialidadRepositorio.save(e);
    }

    private Servicio crearServicio(
            String nombre, String descripcion, ModalidadAtencion modalidad, int duracion, String publico) {
        Servicio s = new Servicio();
        s.setNombre(nombre);
        s.setDescripcion(descripcion);
        s.setModalidad(modalidad);
        s.setDuracionMinutos(duracion);
        s.setPublicoObjetivo(publico);
        return servicioRepositorio.save(s);
    }

    private Psicologo basePsicologo(
            String dni, String nombres, String apellidos, String colegiatura, ModalidadAtencion modalidad) {
        Psicologo p = new Psicologo();
        p.setTipoDocumento(TipoDocumento.DNI);
        p.setNumeroDocumento(dni);
        p.setNombres(nombres);
        p.setApellidos(apellidos);
        p.setColegiatura(colegiatura);
        p.setModalidad(modalidad);
        return p;
    }

    private Psicologo guardarPsicologo(
            Psicologo p, String telefono, String correo, String resumen,
            Set<Especialidad> especialidades, Set<Servicio> servicios) {
        p.setTelefono(telefono);
        p.setCorreo(correo);
        p.setDescripcionProfesional(resumen);
        p.setEspecialidades(new LinkedHashSet<>(especialidades));
        p.setServicios(new LinkedHashSet<>(servicios));
        return psicologoRepositorio.save(p);
    }

    private void crearUsuario(
            String username, String rawPassword, String nombreCompleto,
            String correo, RolUsuario rol, Psicologo psicologo) {
        UsuarioSistema u = new UsuarioSistema();
        u.setUsername(username);
        u.setPasswordHash(passwordEncoder.encode(rawPassword));
        u.setNombreCompleto(nombreCompleto);
        u.setCorreo(correo);
        u.setRol(rol);
        u.setPsicologo(psicologo);
        u.setActivo(true);
        usuarioRepositorio.save(u);
    }

    private Paciente basePaciente(String dni, String nombres, String apellidos, LocalDate fechaNacimiento) {
        Paciente p = new Paciente();
        p.setTipoDocumento(TipoDocumento.DNI);
        p.setNumeroDocumento(dni);
        p.setNombres(nombres);
        p.setApellidos(apellidos);
        p.setFechaNacimiento(fechaNacimiento);
        p.setDireccion("Huánuco, Perú");
        return p;
    }

    private Paciente guardarPaciente(
            Paciente p, String telefono, String correo, String apoderado, boolean consentimiento) {
        p.setTelefono(telefono);
        p.setCorreo(correo);
        p.setApoderado(apoderado);
        p.setConsentimientoRegistrado(consentimiento);
        return pacienteRepositorio.save(p);
    }

    private Cita baseCita(
            Paciente paciente, Psicologo psicologo, Servicio servicio, LocalDate fecha, LocalTime horaInicio) {
        Cita c = new Cita();
        c.setPaciente(paciente);
        c.setPsicologo(psicologo);
        c.setServicio(servicio);
        c.setFecha(fecha);
        c.setHoraInicio(horaInicio);
        c.setDuracionMinutos(servicio.getDuracionMinutos());
        return c;
    }

    private Cita conNota(
            Cita c, ModalidadAtencion modalidad, EstadoCita estado,
            String motivo, String notaEvolucion, String diagnostico) {
        c.setModalidad(modalidad);
        c.setEstado(estado);
        c.setMotivoConsulta(motivo);
        c.setNotaEvolucion(notaEvolucion);
        c.setDiagnosticoReferencial(diagnostico);
        return c;
    }
}
