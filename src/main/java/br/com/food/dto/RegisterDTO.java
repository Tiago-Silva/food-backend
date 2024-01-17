package br.com.food.dto;

import br.com.food.enuns.UserRole;
import br.com.food.enuns.UserType;

import java.util.List;

public record RegisterDTO(
    String nome,
    String sobreNome,
    String login,
    String password,
    String telefone,
    String endereco,
    UserType type,
    UserRole role,
    Boolean accountNonExpired,
    Boolean accountNonLocked,
    Boolean credentialsNonExpired,
    Boolean enabled
) {
}
