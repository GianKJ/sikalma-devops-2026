package pe.com.sikalma.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.com.sikalma.entity.Cita;
import pe.com.sikalma.entity.EstadoCita;

public interface CitaRepositorio extends JpaRepository<Cita, Long> {

    @Query("""
        select c from Cita c
        join fetch c.paciente
        join fetch c.psicologo
        join fetch c.servicio
        order by c.fechaHoraInicio desc
        """)
    List<Cita> listarConDetalle();

    @Query("""
        select count(c) > 0 from Cita c
        where c.psicologo.id = :psicologoId
          and c.estado <> :cancelada
          and c.fechaHoraInicio < :fin
          and c.fechaHoraFin > :inicio
        """)
    boolean existeCruce(@Param("psicologoId") Long psicologoId,
                        @Param("inicio") LocalDateTime inicio,
                        @Param("fin") LocalDateTime fin,
                        @Param("cancelada") EstadoCita cancelada);
}