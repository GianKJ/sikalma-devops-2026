package pe.com.sikalma.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.sikalma.dto.ServicioFormulario;
import pe.com.sikalma.entity.Servicio;
import pe.com.sikalma.exception.RegistroDuplicadoException;
import pe.com.sikalma.exception.RegistroNoEncontradoException;
import pe.com.sikalma.repository.ServicioRepositorio;

@Service
public class ServicioPsicologicoServicio {

    private final ServicioRepositorio repositorio;

    public ServicioPsicologicoServicio(ServicioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<Servicio> listar(String busqueda) {
        if (busqueda == null || busqueda.isBlank()) {
            return repositorio.findAllByOrderByNombreAsc();
        }
        return repositorio.findByNombreContainingIgnoreCaseOrderByNombreAsc(busqueda.trim());
    }

    @Transactional(readOnly = true)
    public List<Servicio> listarActivos() {
        return repositorio.findByActivoTrueOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public ServicioFormulario obtenerFormulario(Long id) {
        Servicio entidad = obtener(id);
        ServicioFormulario formulario = new ServicioFormulario();
        formulario.setId(entidad.getId());
        formulario.setNombre(entidad.getNombre());
        formulario.setDescripcion(entidad.getDescripcion());
        formulario.setModalidad(entidad.getModalidad());
        formulario.setDuracionMinutos(entidad.getDuracionMinutos());
        formulario.setPublicoObjetivo(entidad.getPublicoObjetivo());
        return formulario;
    }

    @Transactional
    public void guardar(ServicioFormulario formulario) {
        String nombre = formulario.getNombre().trim();
        repositorio.findByNombreIgnoreCase(nombre)
                .filter(existente -> !Objects.equals(existente.getId(), formulario.getId()))
                .ifPresent(existente -> {
                    throw new RegistroDuplicadoException("nombre", "Ya existe un servicio con este nombre.");
                });

        Servicio entidad = formulario.getId() == null ? new Servicio() : obtener(formulario.getId());
        entidad.setNombre(nombre);
        entidad.setDescripcion(formulario.getDescripcion().trim());
        entidad.setModalidad(formulario.getModalidad());
        entidad.setDuracionMinutos(formulario.getDuracionMinutos());
        entidad.setPublicoObjetivo(formulario.getPublicoObjetivo().trim());
        repositorio.save(entidad);
    }

    @Transactional
    public void alternarEstado(Long id) {
        Servicio entidad = obtener(id);
        entidad.setActivo(!entidad.isActivo());
    }

    private Servicio obtener(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RegistroNoEncontradoException("El servicio solicitado no existe."));
    }
}
