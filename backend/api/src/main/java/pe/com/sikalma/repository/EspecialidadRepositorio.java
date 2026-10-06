package pe.com.sikalma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.sikalma.entity.Especialidad;

public interface EspecialidadRepositorio extends JpaRepository<Especialidad, Long> {

    List<Especialidad> findAllByOrderByNombreAsc();

    List<Especialidad> findByActivoTrueOrderByNombreAsc();

    List<Especialidad> findByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);

    Optional<Especialidad> findByNombreIgnoreCase(String nombre);
}
