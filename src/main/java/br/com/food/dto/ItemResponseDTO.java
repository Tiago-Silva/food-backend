package br.com.food.dto;


public record ItemResponseDTO(Long iditem,
                              int quantidade,
                              String descricao,
                              int idproduto,
                              Long idpedido) {
}
