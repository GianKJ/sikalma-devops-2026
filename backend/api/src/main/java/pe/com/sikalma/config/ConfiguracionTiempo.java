package pe.com.sikalma.config;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionTiempo {

    @Bean
    Clock relojAplicacion(PropiedadesSikalma propiedades) {
        ZoneId zonaHoraria = ZoneId.of(propiedades.institucion().zonaHoraria());
        return Clock.system(zonaHoraria);
    }
}
