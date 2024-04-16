package br.com.food.dto;


import br.com.food.entity.Item;
import br.com.food.enuns.PedidoStatus;
import br.com.food.enuns.PedidoType;
import br.com.food.enuns.TipoPagamento;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record PedidoResponseDTO(
        Long idpedido,
        Date data,
        String ano,
        String mes,
        String dia,
        String hora,
        BigDecimal total,
        String userName,
        String iduser,
        TipoPagamento tipoPagamento,
        PedidoStatus status,
        PedidoType type,
        List<ItemResponseDTO> itemsReponseDTO
) { }
