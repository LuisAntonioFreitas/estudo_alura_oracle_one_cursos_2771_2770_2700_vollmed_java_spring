package net.lanet.vollmed.domain.authentication;

import net.lanet.vollmed.domain.usuario.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    private IUsuarioRepository repositoryUsuario;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDetails item = repositoryUsuario.findFirstByLoginOrEmail(login, login);
        if (item != null) {
            if (item.isEnabled()) {
                return item;
            } else { throw new AccessDeniedException("Acesso negado."); }
        }
        throw new BadCredentialsException("");
    }
}
