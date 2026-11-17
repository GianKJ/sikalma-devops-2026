package pe.com.sikalma.service;

import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.sikalma.dto.UsuarioFormulario;
import pe.com.sikalma.entity.Psicologo;
import pe.com.sikalma.entity.UsuarioSistema;
import pe.com.sikalma.exception.RegistroDuplicadoException;
import pe.com.sikalma.exception.RegistroNoEncontradoException;
import pe.com.sikalma.repository.PsicologoRepositorio;
import pe.com.sikalma.repository.UsuarioSistemaRepositorio;

@Service
public class UsuarioSistemaServicio {

    private final UsuarioSistemaRepositorio repositorio;
    private final PsicologoRepositorio psicologoRepositorio;
    private final PasswordEncoder passwordEncoder;

    public UsuarioSistemaServicio(
            UsuarioSistemaRepositorio repositorio,
            PsicologoRepositorio psicologoRepositorio,
            PasswordEncoder passwordEncoder) {
        this.repositorio = repositorio;
        this.psicologoRepositorio = psicologoRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UsuarioSistema> listar(String busqueda) {
        if (busqueda == null || busqueda.isBlank()) {
            return repositorio.findAllByOrderByNombreCompletoAsc();
        }
        String filtro = busqueda.trim();
        return repositorio
                .findByUsernameContainingIgnoreCaseOrNombreCompletoContainingIgnoreCaseOrCorreoContainingIgnoreCaseOrderByNombreCompletoAsc(
                        filtro, filtro, filtro);
    }

    @Transactional(readOnly = true)
    public UsuarioFormulario obtenerFormulario(Long id) {
        UsuarioSistema entidad = obtener(id);
        UsuarioFormulario formulario = new UsuarioFormulario();
        formulario.setId(entidad.getId());
        formulario.setUsername(entidad.getUsername());
        formulario.setNombreCompleto(entidad.getNombreCompleto());
        formulario.setCorreo(entidad.getCorreo());
        formulario.setRol(entidad.getRol());
        formulario.setPsicologoId(entidad.getPsicologo() == null ? null : entidad.getPsicologo().getId());
        formulario.setActivo(entidad.isActivo());
        return formulario;
    }

    @Transactional
    public void guardar(UsuarioFormulario formulario) {
        String username = formulario.getUsername().trim().toLowerCase(Locale.ROOT);
        TextoNormalizadorUtil.validarDuplicado(
                repositorio.findByUsernameIgnoreCase(username).orElse(null),
                formulario.getId(),
                UsuarioSistema::getId,
                "username",
                "Ya existe una cuenta registrada con este usuario.");

        String correo = TextoNormalizadorUtil.opcionalMinuscula(formulario.getCorreo());
        if (correo != null) {
            TextoNormalizadorUtil.validarDuplicado(
                    repositorio.findByCorreoIgnoreCase(correo).orElse(null),
                    formulario.getId(),
                    UsuarioSistema::getId,
                    "correo",
                    "Ya existe una cuenta registrada con este correo.");
        }

        String claveIngresada = TextoNormalizadorUtil.opcional(formulario.getPassword());
        if (formulario.getId() == null && claveIngresada == null) {
            throw new RegistroDuplicadoException("password", "Ingresa una contraseña inicial de al menos 6 caracteres.");
        }
        if (claveIngresada != null && claveIngresada.length() < 6) {
            throw new RegistroDuplicadoException("password", "La contraseña debe tener al menos 6 caracteres.");
        }

        UsuarioSistema entidad = formulario.getId() == null ? new UsuarioSistema() : obtener(formulario.getId());
        entidad.setUsername(username);
        entidad.setNombreCompleto(formulario.getNombreCompleto().trim());
        entidad.setCorreo(correo);
        entidad.setRol(formulario.getRol());
        entidad.setActivo(formulario.isActivo());

        Psicologo psicologo = formulario.getPsicologoId() == null
                ? null
                : psicologoRepositorio.findById(formulario.getPsicologoId()).orElse(null);
        entidad.setPsicologo(psicologo);

        if (claveIngresada != null) {
            entidad.setPasswordHash(passwordEncoder.encode(claveIngresada));
        }

        repositorio.save(entidad);
    }

    @Transactional
    public void alternarEstado(Long id) {
        UsuarioSistema entidad = obtener(id);
        entidad.setActivo(!entidad.isActivo());
    }

    private UsuarioSistema obtener(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RegistroNoEncontradoException("El usuario solicitado no existe."));
    }
}
