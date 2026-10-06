// entity/Cita.java
package pe.com.sikalma.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "citas")
public class Cita extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "psicologo_id", nullable = false)
    private Psicologo psicologo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin", nullable = false)
    private LocalDateTime fechaHoraFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @Column(length = 500)
    private String observaciones;

    // getters y setters de todos los campos (igual que en Paciente)
}