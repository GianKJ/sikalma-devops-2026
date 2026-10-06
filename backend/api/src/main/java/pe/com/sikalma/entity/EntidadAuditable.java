package pe.com.sikalma.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public abstract class EntidadAuditable {

    private static final ZoneId ZONA_HUANUCO = ZoneId.of("America/Lima");

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void antesDeCrear() {
        LocalDateTime ahora = LocalDateTime.now(ZONA_HUANUCO);
        fechaCreacion = ahora;
        fechaActualizacion = ahora;
    }

    @PreUpdate
    void antesDeActualizar() {
        fechaActualizacion = LocalDateTime.now(ZONA_HUANUCO);
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
}
