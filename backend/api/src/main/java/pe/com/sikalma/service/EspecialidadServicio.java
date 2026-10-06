package pe.com.sikalma.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.sikalma.dto.EspecialidadFormulario;
import pe.com.sikalma.entity.Especialidad;
import pe.com.sikalma.exception.RegistroDuplicadoException;
import pe.com.sikalma.exception.RegistroNoEncontradoException;
import pe.com.sikalma.repository.EspecialidadRepositorio;

@Service
public class EspecialidadServicio {

    private final EspecialidadRepositorio repositorio;

    public EspecialidadServicio(EspecialidadRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<Especialidad> listar(String busqueda) {
        if (busqueda == null || busqueda.isBlank()) {
            return repositorio.findAllByOrderByNombreAsc();
        }
        return repositorio.findByNombreContainingIgnoreCaseOrderByNombreAsc(busqueda.trim());
    }

    @Transactional(readOnly = true)
    public List<Especialidad> listarActivas() {
        return repositorio.findByActivoTrueOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public EspecialidadFormulario obtenerFormulario(Long id) {
        Especialidad entidad = obtener(id);
        EspecialidadFormulario formulario = new EspecialidadFormulario();
        formulario.setId(entidad.getId());
        formulario.setNombre(entidad.getNombre());
        formulario.setDescripcion(entidad.getDescripcion());
        return formulario;
    }

    @Transactional
    public void guardar(EspecialidadFormulario formulario) {
        String nombre = formulario.getNombre().trim();
        repositorio.findByNombreIgnoreCase(nombre)
                .filter(existente -> !Objects.equals(existente.getId(), formulario.getId()))
                .ifPresent(existente -> {
                    throw new RegistroDuplicadoException("nombre", "Ya existe una especialidad con este nombre.");
                });

        Especialidad entidad = formulario.getId() == null ? new Especialidad() : obtener(formulario.getId());
        entidad.setNombre(nombre);
        entidad.setDescripcion(opcional(formulario.getDescripcion()));
        repositorio.save(entidad);
    }

    @Transactional
    public void alternarEstado(Long id) {
        Especialidad entidad = obtener(id);
        entidad.setActivo(!entidad.isActivo());
    }

    private Especialidad obtener(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RegistroNoEncontradoException("La especialidad solicitada no existe."));
    }

    private String opcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
