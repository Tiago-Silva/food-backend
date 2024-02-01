package br.com.food.dto;


public record ItemRequestDTO(
    int quantidade,
    String descricao,
    int idproduto,
    Long idpedido
) {
}
