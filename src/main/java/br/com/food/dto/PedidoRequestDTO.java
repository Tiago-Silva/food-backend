package br.com.food.dto;


import br.com.food.enuns.PedidoStatus;
import br.com.food.enuns.TipoPagamento;
import java.math.BigDecimal;
import java.util.List;

public record PedidoRequestDTO(
        BigDecimal total,
        String iduser,
        TipoPagamento tipoPagamento,
        PedidoStatus status,
        List<ItemRequestDTO> itemRequestDTOS
) {
}
