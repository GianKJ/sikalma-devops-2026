package pe.com.sikalma.security;

import java.io.IOException;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.com.sikalma.entity.RolUsuario;

@Component
public class ManejadorIngresoExitoso implements AuthenticationSuccessHandler {

    public static final String ATTR_BIENVENIDA_PENDIENTE = "SIKALMA_BIENVENIDA_PENDIENTE";

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
        HttpSession session = request.getSession(true);
        session.setAttribute(ATTR_BIENVENIDA_PENDIENTE, Boolean.TRUE);

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String destino = resolverDestino(request.getContextPath(), roles);
        response.sendRedirect(destino);
    }

    private String resolverDestino(String contextPath, Set<String> roles) {
        for (RolUsuario rol : RolUsuario.values()) {
            if (roles.contains("ROLE_" + rol.name())) {
                return contextPath + rol.getRutaInicio();
            }
        }
        return contextPath + RolUsuario.ADMINISTRADOR.getRutaInicio();
    }
}
