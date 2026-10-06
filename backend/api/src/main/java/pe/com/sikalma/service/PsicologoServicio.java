package pe.com.sikalma.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.sikalma.dto.PsicologoFormulario;
import pe.com.sikalma.entity.Psicologo;
import pe.com.sikalma.exception.RegistroDuplicadoException;
import pe.com.sikalma.exception.RegistroNoEncontradoException;
import pe.com.sikalma.repository.EspecialidadRepositorio;
import pe.com.sikalma.repository.PsicologoRepositorio;
import pe.com.sikalma.repository.ServicioRepositorio;

@Service
public class PsicologoServicio {

    private final PsicologoRepositorio repositorio;
    private final EspecialidadRepositorio especialidadRepositorio;
    private final ServicioRepositorio servicioRepositorio;

    public PsicologoServicio(
            PsicologoRepositorio repositorio,
            EspecialidadRepositorio especialidadRepositorio,
            ServicioRepositorio servicioRepositorio) {
        this.repositorio = repositorio;
        this.especialidadRepositorio = especialidadRepositorio;
        this.servicioRepositorio = servicioRepositorio;
    }

    @Transactional(readOnly = true)
    public List<Psicologo> listar(String busqueda) {
        if (busqueda == null || busqueda.isBlank()) {
            return repositorio.findAllByOrderByApellidosAscNombresAsc();
        }
        String filtro = busqueda.trim();
        return repositorio
                .findByNumeroDocumentoContainingIgnoreCaseOrNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrderByApellidosAscNombresAsc(
                        filtro, filtro, filtro);
    }

    @Transactional(readOnly = true)
    public PsicologoFormulario obtenerFormulario(Long id) {
        Psicologo entidad = obtener(id);
        PsicologoFormulario formulario = new PsicologoFormulario();
        formulario.setId(entidad.getId());
        formulario.setTipoDocumento(entidad.getTipoDocumento());
        formulario.setNumeroDocumento(entidad.getNumeroDocumento());
        formulario.setNombres(entidad.getNombres());
        formulario.setApellidos(entidad.getApellidos());
        formulario.setColegiatura(entidad.getColegiatura());
        formulario.setTelefono(entidad.getTelefono());
        formulario.setCorreo(entidad.getCorreo());
        formulario.setDescripcionProfesional(entidad.getDescripcionProfesional());
        formulario.setModalidad(entidad.getModalidad());
        formulario.setEspecialidadIds(entidad.getEspecialidades().stream().map(e -> e.getId()).collect(
                java.util.stream.Collectors.toCollection(LinkedHashSet::new)));
        formulario.setServicioIds(entidad.getServicios().stream().map(s -> s.getId()).collect(
                java.util.stream.Collectors.toCollection(LinkedHashSet::new)));
        return formulario;
    }

    @Transactional
    public void guardar(PsicologoFormulario formulario) {
        String documento = formulario.getNumeroDocumento().trim().toUpperCase(Locale.ROOT);
        validarDuplicado(repositorio.findByNumeroDocumentoIgnoreCase(documento).orElse(null), formulario.getId(),
                "numeroDocumento", "Ya existe un psicólogo con este documento.");

        String correo = opcionalMinuscula(formulario.getCorreo());
        if (correo != null) {
            validarDuplicado(repositorio.findByCorreoIgnoreCase(correo).orElse(null), formulario.getId(),
                    "correo", "Ya existe un psicólogo con este correo.");
        }

        String colegiatura = opcionalMayuscula(formulario.getColegiatura());
        if (colegiatura != null) {
            validarDuplicado(repositorio.findByColegiaturaIgnoreCase(colegiatura).orElse(null), formulario.getId(),
                    "colegiatura", "Ya existe un psicólogo con esta colegiatura.");
        }

        Psicologo entidad = formulario.getId() == null ? new Psicologo() : obtener(formulario.getId());
        entidad.setTipoDocumento(formulario.getTipoDocumento());
        entidad.setNumeroDocumento(documento);
        entidad.setNombres(formulario.getNombres().trim());
        entidad.setApellidos(formulario.getApellidos().trim());
        entidad.setColegiatura(colegiatura);
        entidad.setTelefono(formulario.getTelefono().trim());
        entidad.setCorreo(correo);
        entidad.setDescripcionProfesional(opcional(formulario.getDescripcionProfesional()));
        entidad.setModalidad(formulario.getModalidad());
        entidad.setEspecialidades(new LinkedHashSet<>(especialidadRepositorio.findAllById(formulario.getEspecialidadIds())));
        entidad.setServicios(new LinkedHashSet<>(servicioRepositorio.findAllById(formulario.getServicioIds())));
        repositorio.save(entidad);
    }

    @Transactional
    public void alternarEstado(Long id) {
        Psicologo entidad = obtener(id);
        entidad.setActivo(!entidad.isActivo());
    }

    private Psicologo obtener(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RegistroNoEncontradoException("El psicólogo solicitado no existe."));
    }

    private void validarDuplicado(Psicologo existente, Long idActual, String campo, String mensaje) {
        if (existente != null && !Objects.equals(existente.getId(), idActual)) {
            throw new RegistroDuplicadoException(campo, mensaje);
        }
    }

    private String opcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private String opcionalMinuscula(String valor) {
        String normalizado = opcional(valor);
        return normalizado == null ? null : normalizado.toLowerCase(Locale.ROOT);
    }

    private String opcionalMayuscula(String valor) {
        String normalizado = opcional(valor);
        return normalizado == null ? null : normalizado.toUpperCase(Locale.ROOT);
    }
}
