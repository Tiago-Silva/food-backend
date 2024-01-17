package br.com.food.infra.security;

import br.com.food.entity.User;
import br.com.food.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> usuario = this.userRepository.getUserByLoginOptional(username);

        if (usuario.isPresent()) {
            return new ResourceOwner(usuario.get(),this.getGrupos(usuario.get()));
        } else {
            throw new UsernameNotFoundException("usuario não autorizado");
        }
    }

    private Collection<? extends GrantedAuthority> getGrupos(User usuario) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(usuario.getRole().toString()));
        return authorities;
    }
}
