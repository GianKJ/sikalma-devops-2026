package pe.com.sikalma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.sikalma.entity.Paciente;

public interface PacienteRepositorio extends JpaRepository<Paciente, Long> {

    List<Paciente> findAllByOrderByApellidosAscNombresAsc();

    List<Paciente> findByNumeroDocumentoContainingIgnoreCaseOrNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrderByApellidosAscNombresAsc(
            String documento, String nombres, String apellidos);

    Optional<Paciente> findByNumeroDocumentoIgnoreCase(String numeroDocumento);

    Optional<Paciente> findByCorreoIgnoreCase(String correo);
}
