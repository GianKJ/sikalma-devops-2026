package pe.com.sikalma.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.sikalma.entity.UsuarioSistema;
import pe.com.sikalma.repository.UsuarioSistemaRepositorio;

@Service
public class UsuarioDetallesServicio implements UserDetailsService {

    private final UsuarioSistemaRepositorio repositorio;

    public UsuarioDetallesServicio(UsuarioSistemaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioSistema usuario = repositorio.findByUsernameIgnoreCase(username == null ? "" : username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado."));

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPasswordHash())
                .roles(usuario.getRol().name())
                .disabled(!usuario.isActivo())
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioSistema> buscarPorUsername(String username) {
        if (username == null || username.isBlank()) {
            return Optional.empty();
        }
        return repositorio.findByUsernameIgnoreCase(username.trim());
    }
}
