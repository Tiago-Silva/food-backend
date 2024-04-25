package br.com.food.enuns;

import lombok.Getter;

@Getter
public enum PedidoStatus {
    PENDENTE(1),
    RECEBIDO(2),
    PREPARADO(3),
    PRONTO(4),
    ENVIADO(5),
    ENTREGUE(6),
    CANCELADO(7),
    FINALIZADO(8);

    private final int order;

    PedidoStatus(int order) {
        this.order = order;
    }
}