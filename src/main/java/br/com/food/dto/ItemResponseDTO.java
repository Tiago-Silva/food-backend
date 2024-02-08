package br.com.food.dto;


import java.math.BigDecimal;

public record ItemResponseDTO(
    Long iditem,
    int quantidade,
    String descricao,
    BigDecimal valor,
    BigDecimal total,
    int idproduto,
    Long idpedido
) {
}
