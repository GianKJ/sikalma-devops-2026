package pe.com.sikalma.service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.sikalma.dto.PacienteFormulario;
import pe.com.sikalma.entity.Paciente;
import pe.com.sikalma.exception.RegistroDuplicadoException;
import pe.com.sikalma.exception.RegistroNoEncontradoException;
import pe.com.sikalma.repository.PacienteRepositorio;

@Service
public class PacienteServicio {

    private final PacienteRepositorio repositorio;

    public PacienteServicio(PacienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<Paciente> listar(String busqueda) {
        if (busqueda == null || busqueda.isBlank()) {
            return repositorio.findAllByOrderByApellidosAscNombresAsc();
        }
        String filtro = busqueda.trim();
        return repositorio
                .findByNumeroDocumentoContainingIgnoreCaseOrNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrderByApellidosAscNombresAsc(
                        filtro, filtro, filtro);
    }

    @Transactional(readOnly = true)
    public PacienteFormulario obtenerFormulario(Long id) {
        Paciente entidad = obtener(id);
        PacienteFormulario formulario = new PacienteFormulario();
        formulario.setId(entidad.getId());
        formulario.setTipoDocumento(entidad.getTipoDocumento());
        formulario.setNumeroDocumento(entidad.getNumeroDocumento());
        formulario.setNombres(entidad.getNombres());
        formulario.setApellidos(entidad.getApellidos());
        formulario.setFechaNacimiento(entidad.getFechaNacimiento());
        formulario.setTelefono(entidad.getTelefono());
        formulario.setCorreo(entidad.getCorreo());
        formulario.setDireccion(entidad.getDireccion());
        formulario.setContactoEmergencia(entidad.getContactoEmergencia());
        formulario.setApoderado(entidad.getApoderado());
        formulario.setObservacionesAdministrativas(entidad.getObservacionesAdministrativas());
        formulario.setConsentimientoRegistrado(entidad.isConsentimientoRegistrado());
        formulario.setPreferenciasPrivacidad(entidad.getPreferenciasPrivacidad());
        return formulario;
    }

    @Transactional
    public void guardar(PacienteFormulario formulario) {
        String documento = formulario.getNumeroDocumento().trim().toUpperCase(Locale.ROOT);
        validarDuplicado(repositorio.findByNumeroDocumentoIgnoreCase(documento).orElse(null), formulario.getId(),
                "numeroDocumento", "Ya existe un paciente con este documento.");

        String correo = opcionalMinuscula(formulario.getCorreo());
        if (correo != null) {
            validarDuplicado(repositorio.findByCorreoIgnoreCase(correo).orElse(null), formulario.getId(),
                    "correo", "Ya existe un paciente con este correo.");
        }

        Paciente entidad = formulario.getId() == null ? new Paciente() : obtener(formulario.getId());
        entidad.setTipoDocumento(formulario.getTipoDocumento());
        entidad.setNumeroDocumento(documento);
        entidad.setNombres(formulario.getNombres().trim());
        entidad.setApellidos(formulario.getApellidos().trim());
        entidad.setFechaNacimiento(formulario.getFechaNacimiento());
        entidad.setTelefono(formulario.getTelefono().trim());
        entidad.setCorreo(correo);
        entidad.setDireccion(opcional(formulario.getDireccion()));
        entidad.setContactoEmergencia(opcional(formulario.getContactoEmergencia()));
        entidad.setApoderado(opcional(formulario.getApoderado()));
        entidad.setObservacionesAdministrativas(opcional(formulario.getObservacionesAdministrativas()));
        entidad.setConsentimientoRegistrado(formulario.isConsentimientoRegistrado());
        entidad.setPreferenciasPrivacidad(opcional(formulario.getPreferenciasPrivacidad()));
        repositorio.save(entidad);
    }

    @Transactional
    public void alternarEstado(Long id) {
        Paciente entidad = obtener(id);
        entidad.setActivo(!entidad.isActivo());
    }

    private Paciente obtener(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RegistroNoEncontradoException("El paciente solicitado no existe."));
    }

    private void validarDuplicado(Paciente existente, Long idActual, String campo, String mensaje) {
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
}
