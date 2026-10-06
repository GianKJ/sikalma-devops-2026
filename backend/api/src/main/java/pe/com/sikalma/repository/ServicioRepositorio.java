package pe.com.sikalma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.sikalma.entity.Servicio;

public interface ServicioRepositorio extends JpaRepository<Servicio, Long> {

    List<Servicio> findAllByOrderByNombreAsc();

    List<Servicio> findByActivoTrueOrderByNombreAsc();

    List<Servicio> findByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);

    Optional<Servicio> findByNombreIgnoreCase(String nombre);
}
