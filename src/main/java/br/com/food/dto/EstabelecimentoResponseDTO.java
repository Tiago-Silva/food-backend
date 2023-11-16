package br.com.food.dto;

public record EstabelecimentoResponseDTO(int idestabelecimento,
                                         String razaoSocial,
                                         String nomeFantasia,
                                         String cnpj,
                                         String cpf,
                                         String pais,
                                         String estado,
                                         String cidade,
                                         String bairro,
                                         String endereco,
                                         String telefone) {
}
