package br.com.food.dto;

import br.com.food.enuns.UserRole;
import br.com.food.enuns.UserType;

public record UserRequestDTO( String nome,
                             String sobreNome,
                             String login,
                             String password,
                             String telefone,
                             String endereco,
                             String cpf,
                             String email,
                             UserType type,
                             UserRole role,
                             Boolean accountNonExpired,
                             Boolean accountNonLocked,
                             Boolean credentialsNonExpired,
                             Boolean enabled,
                             int idestabelecimento) {
}
