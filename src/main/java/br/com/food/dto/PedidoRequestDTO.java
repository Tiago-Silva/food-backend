package br.com.food.dto;


import br.com.food.entity.Item;
import br.com.food.enuns.TipoPagamento;
import java.math.BigDecimal;
import java.util.List;

public record PedidoRequestDTO( BigDecimal total,
                                String iduser,
                                TipoPagamento tipoPagamento,
                                List<Item> items) {
}
