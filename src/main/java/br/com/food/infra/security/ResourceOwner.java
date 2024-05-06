package br.com.food.infra.security;

import br.com.food.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;

public class ResourceOwner implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private User usuario;

    private Collection<? extends GrantedAuthority> authorities;

    public ResourceOwner(User usuario, Collection<? extends GrantedAuthority> authorities) {
        this.usuario = usuario;
        this.authorities = authorities;
    }

    public String getId() {
        return usuario.getId();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return usuario.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return usuario.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return usuario.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return usuario.getEnabled();
    }
}
