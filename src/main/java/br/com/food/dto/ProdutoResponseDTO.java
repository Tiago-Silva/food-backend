package br.com.food.dto;

import br.com.food.enuns.ProductCategory;

import java.math.BigDecimal;

public record ProdutoResponseDTO(int idproduto,
                                 String nome,
                                 String descricao,
                                 BigDecimal valor,
                                 ProductCategory categoria,
                                 Boolean status,
                                 String urlImage,
                                 Boolean enablePromotions,
                                 int idestabelecimento) {
}
