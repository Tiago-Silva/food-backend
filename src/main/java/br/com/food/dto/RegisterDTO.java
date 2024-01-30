package br.com.food.dto;

import br.com.food.enuns.UserType;

import java.util.Objects;

public record RegisterDTO (
    String nome,
    String sobreNome,
    String email,
    String telefone,
    String endereco,
    UserType type,
    int idestabelecimento
) {
    public void validate() {
        Objects.requireNonNull(idestabelecimento, "idestabelecimento é obrigatório e não pode ser nulo");
        Objects.requireNonNull(email, "email é obrigatório e não pode ser nulo");
        Objects.requireNonNull(nome, "nome é obrigatório e não pode ser nulo");
        Objects.requireNonNull(sobreNome, "sobreNome é obrigatório e não pode ser nulo");
        Objects.requireNonNull(endereco, "endereco é obrigatório e não pode ser nulo");
        Objects.requireNonNull(telefone, "telefone é obrigatório e não pode ser nulo");

        if (idestabelecimento < 0) {
            throw new IllegalArgumentException("idestabelecimento deve ser maior ou igual a zero");
        }
    }
}
