package pe.com.sikalma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.sikalma.entity.Psicologo;

public interface PsicologoRepositorio extends JpaRepository<Psicologo, Long> {

    @EntityGraph(attributePaths = {"especialidades", "servicios"})
    List<Psicologo> findAllByOrderByApellidosAscNombresAsc();

    @EntityGraph(attributePaths = {"especialidades", "servicios"})
    List<Psicologo> findByNumeroDocumentoContainingIgnoreCaseOrNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrderByApellidosAscNombresAsc(
            String documento, String nombres, String apellidos);

    @EntityGraph(attributePaths = {"especialidades", "servicios"})
    Optional<Psicologo> findById(Long id);

    Optional<Psicologo> findByNumeroDocumentoIgnoreCase(String numeroDocumento);

    Optional<Psicologo> findByCorreoIgnoreCase(String correo);

    Optional<Psicologo> findByColegiaturaIgnoreCase(String colegiatura);
}
