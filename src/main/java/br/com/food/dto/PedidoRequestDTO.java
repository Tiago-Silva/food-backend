package br.com.food.dto;


import br.com.food.entity.Item;
import br.com.food.enuns.TipoPagamento;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record PedidoRequestDTO( Date data,

                                 String ano,

                                 String mes,

                                 String dia,

                                 String hora,

                                 BigDecimal total,

                                 String iduser,

                                TipoPagamento tipoPagamento,
                                List<Item> items) {
}
