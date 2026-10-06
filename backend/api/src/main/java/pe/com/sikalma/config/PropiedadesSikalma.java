package pe.com.sikalma.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sikalma")
public record PropiedadesSikalma(
        Institucion institucion,
        Contacto contacto,
        Horario horario,
        List<String> modalidades) {

    public record Institucion(
            String nombre,
            String nombreCorto,
            String lema,
            String ciudad,
            String pais,
            String zonaHoraria,
            String descripcion) {
    }

    public record Contacto(
            String telefono,
            String whatsapp,
            String correo,
            String direccion) {
    }

    public record Horario(String descripcion) {
    }
}
