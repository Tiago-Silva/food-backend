package br.com.food.infra.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;

public class UserSpring extends User {
    @Serial
    private static final long serialVersionUID = 1L;

    private br.com.food.entity.User usuario;
    public UserSpring(
        br.com.food.entity.User usuario,
        Collection<? extends GrantedAuthority> authorities
    ) {
        super(
            usuario.getLogin(),
            usuario.getPassword(),
            usuario.getEnabled(),
            usuario.getAccountNonExpired(),
            usuario.getCredentialsNonExpired(),
            usuario.getAccountNonLocked(),
            authorities
        );
        this.usuario = usuario;
    }

    public br.com.food.entity.User getUsuario() {
        return usuario;
    }
}
