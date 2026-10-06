package pe.com.sikalma.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pe.com.sikalma.config.PropiedadesSikalma;
import pe.com.sikalma.dto.InformacionPublicaDto;

@Service
public class InformacionPublicaServicio {

    private final PropiedadesSikalma propiedades;

    public InformacionPublicaServicio(PropiedadesSikalma propiedades) {
        this.propiedades = propiedades;
    }

    public InformacionPublicaDto obtenerInformacionPublica() {
        PropiedadesSikalma.Institucion institucion = propiedades.institucion();
        PropiedadesSikalma.Contacto contacto = propiedades.contacto();

        return new InformacionPublicaDto(
                institucion.nombre(),
                institucion.nombreCorto(),
                institucion.lema(),
                institucion.descripcion(),
                institucion.ciudad(),
                institucion.pais(),
                contacto.telefono(),
                contacto.correo(),
                contacto.direccion(),
                propiedades.horario().descripcion(),
                "https://wa.me/" + contacto.whatsapp(),
                List.copyOf(propiedades.modalidades()));
    }
}
