package pe.com.sikalma.dto;

import java.util.List;

public record InformacionPublicaDto(
        String nombre,
        String nombreCorto,
        String lema,
        String descripcion,
        String ciudad,
        String pais,
        String telefono,
        String correo,
        String direccion,
        String horario,
        String whatsappUrl,
        List<String> modalidades) {
}
