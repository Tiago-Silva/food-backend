package br.com.food.repository;

import br.com.food.entity.Pedido;
import br.com.food.enuns.TipoPagamento;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PedidoRepository extends GenericRepository {

    public List<Pedido> getAllPedidoByIdEstablishment(int idestabelecimento) {
        return super.getTwoEntitiesByForeignKey(
                Pedido.class,
                "user",
                "estabelecimento",
                "idestabelecimento",
                idestabelecimento,
                "tipoPagamento"
        );
    }

    public List<Pedido> getAllPedidoByEstablishmentAndByPaymentType(int idestabelecimento, String paymentType) {
        return super.getTwoEntitiesByForeignKeyAndWithConditional(
                Pedido.class,
                "user",
                "estabelecimento",
                "idestabelecimento",
                idestabelecimento,
                "ano",
                "tipoPagamento",
                TipoPagamento.valueOf(paymentType)
        );
    }

    public List<Pedido> getAllPedidoByUser(String iduser) {
        return super.getJoinColumn(
                Pedido.class,
                "user",
                "id",
                iduser,
                "tipoPagamento"
        );
    }

    public List<Pedido> getPedidoByUserAndByPaymentType(String iduser, String paymentType) {
        return super.getEntitiesByForeignKeyAndWithConditional(
                Pedido.class,
                "user",
                "id",
                iduser,
                "ano",
                "tipoPagametno",
                TipoPagamento.valueOf(paymentType)
        );
    }
}
