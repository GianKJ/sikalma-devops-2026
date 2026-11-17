package pe.com.sikalma.security;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class ConfiguracionSeguridad {

    private static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";
    private static final String ROL_RECEPCIONISTA = "RECEPCIONISTA";
    private static final String ROL_PSICOLOGO = "PSICOLOGO";
    private static final String ROL_FISIOTERAPEUTA = "FISIOTERAPEUTA";
    private static final String RUTA_LOGIN = "/login";

    private final ManejadorIngresoExitoso manejadorIngresoExitoso;

    public ConfiguracionSeguridad(ManejadorIngresoExitoso manejadorIngresoExitoso) {
        this.manejadorIngresoExitoso = manejadorIngresoExitoso;
    }

    @Bean
    SecurityFilterChain filtroSeguridad(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(autorizacion -> autorizacion
                        .requestMatchers(
                                "/", "/inicio", RUTA_LOGIN, "/acceso-denegado",
                                "/css/**", "/js/**", "/img/**", "/video/**", "/favicon.ico",
                                "/error", "/error/**")
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole(ROL_ADMINISTRADOR)
                        .requestMatchers("/recepcion/**")
                        .hasAnyRole(ROL_ADMINISTRADOR, ROL_RECEPCIONISTA)
                        .requestMatchers("/profesional/**")
                        .hasAnyRole(ROL_ADMINISTRADOR, ROL_PSICOLOGO, ROL_FISIOTERAPEUTA)
                        .anyRequest().authenticated())
                .formLogin(formulario -> formulario
                        .loginPage(RUTA_LOGIN)
                        .successHandler(manejadorIngresoExitoso)
                        .failureUrl("/login?error")
                        .permitAll())
                .logout(salida -> salida
                        .logoutSuccessUrl("/?sesion-cerrada")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .exceptionHandling(excepcion -> excepcion
                        .accessDeniedPage("/acceso-denegado"))
                .sessionManagement(sesion -> sesion
                        .sessionFixation(fijacion -> fijacion.migrateSession())
                        .maximumSessions(1))
                .headers(cabeceras -> cabeceras
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; img-src 'self' data:; media-src 'self'; "
                                        + "style-src 'self'; script-src 'self'; font-src 'self'; "
                                        + "form-action 'self'")));

        return http.build();
    }

    @Bean
    PasswordEncoder codificadorContrasenas() {
        Map<String, PasswordEncoder> codificadores = Map.of("bcrypt", new BCryptPasswordEncoder(12));
        return new DelegatingPasswordEncoder("bcrypt", codificadores);
    }
}
