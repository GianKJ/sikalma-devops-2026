package pe.com.sikalma.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.sikalma.entity.Cita;
import pe.com.sikalma.entity.EstadoCita;

public interface CitaRepositorio extends JpaRepository<Cita, Long> {

    @EntityGraph(attributePaths = {"paciente", "psicologo", "servicio"})
    List<Cita> findAllByOrderByFechaDescHoraInicioAsc();

    @EntityGraph(attributePaths = {"paciente", "psicologo", "servicio"})
    List<Cita> findByPsicologoIdOrderByFechaDescHoraInicioAsc(Long psicologoId);

    @EntityGraph(attributePaths = {"paciente", "psicologo", "servicio"})
    List<Cita> findByFechaOrderByHoraInicioAsc(LocalDate fecha);

    @EntityGraph(attributePaths = {"paciente", "psicologo", "servicio"})
    List<Cita> findByPsicologoIdAndFechaAndEstadoNotOrderByHoraInicioAsc(
            Long psicologoId, LocalDate fecha, EstadoCita estadoExcluido);

    @Override
    @EntityGraph(attributePaths = {"paciente", "psicologo", "servicio"})
    Optional<Cita> findById(Long id);
}
