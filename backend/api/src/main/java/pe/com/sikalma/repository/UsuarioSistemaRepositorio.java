package pe.com.sikalma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.sikalma.entity.UsuarioSistema;

public interface UsuarioSistemaRepositorio extends JpaRepository<UsuarioSistema, Long> {

    @EntityGraph(attributePaths = {"psicologo"})
    Optional<UsuarioSistema> findByUsernameIgnoreCase(String username);

    Optional<UsuarioSistema> findByCorreoIgnoreCase(String correo);

    @Override
    @EntityGraph(attributePaths = {"psicologo"})
    Optional<UsuarioSistema> findById(Long id);

    @EntityGraph(attributePaths = {"psicologo"})
    List<UsuarioSistema> findAllByOrderByNombreCompletoAsc();

    @EntityGraph(attributePaths = {"psicologo"})
    List<UsuarioSistema> findByUsernameContainingIgnoreCaseOrNombreCompletoContainingIgnoreCaseOrCorreoContainingIgnoreCaseOrderByNombreCompletoAsc(
            String username, String nombreCompleto, String correo);

    long countByActivoTrue();
}
