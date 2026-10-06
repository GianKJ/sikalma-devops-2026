package pe.com.sikalma.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class ConfiguracionSeguridad {

    @Bean
    SecurityFilterChain filtroSeguridad(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(autorizacion -> autorizacion
                        .requestMatchers(
                                "/", "/inicio", "/login", "/acceso-denegado",
                                "/css/**", "/js/**", "/img/**", "/favicon.ico",
                                "/error", "/error/**")
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/recepcion/**")
                        .hasAnyRole("ADMINISTRADOR", "RECEPCIONISTA")
                        .requestMatchers("/profesional/**")
                        .hasAnyRole("ADMINISTRADOR", "PSICOLOGO", "FISIOTERAPEUTA")
                        .anyRequest().authenticated())
                .formLogin(formulario -> formulario
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/citas", true)
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
                                "default-src 'self'; img-src 'self' data:; "
                                        + "style-src 'self'; script-src 'self'; font-src 'self'; "
                                        + "form-action 'self'")));

        return http.build();
    }

    @Bean
    PasswordEncoder codificadorContrasenas() {
        Map<String, PasswordEncoder> codificadores = new HashMap<>();
        codificadores.put("bcrypt", new BCryptPasswordEncoder(12));
        codificadores.put("noop", NoOpPasswordEncoder.getInstance());

        DelegatingPasswordEncoder delegador = new DelegatingPasswordEncoder("bcrypt", codificadores);
        delegador.setDefaultPasswordEncoderForMatches(NoOpPasswordEncoder.getInstance());
        return delegador;
    }
}
