package br.com.food.repository;

import br.com.food.entity.Pedido;
import br.com.food.enuns.TipoPagamento;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    public List<Pedido> getAllPedidoByUserWithDataAndPagination(
            String iduser,
            Date startDate,
            Date endDate,
            int pageNumber,
            int pageSize
    ) {
        return super.getJoinColumnWithDateAndPagination(
                Pedido.class,
                "user",
                "id",
                iduser,
                "data",
                startDate,
                endDate,
                "tipoPagamento",
                pageNumber,
                pageSize
        );
    }

    public List<Pedido> getAllPedidoEstablishmentWithDataAndPagination(
            int idEstabelecimento,
            Date startDate,
            Date endDate,
            int pageNumber,
            int pageSize
    ) {
        return super.getTwoEntitiesByForeignKeyWithDateAndPagination(
                Pedido.class,
                "user",
                "estabelecimento",
                "idestabelecimento",
                idEstabelecimento,
                "data",
                startDate,
                endDate,
                "data",
                pageNumber,
                pageSize
        );
    }

    public List<Pedido> getPedidoByUserAndByPaymentType(String iduser, String paymentType) {
        return super.getEntitiesByForeignKeyAndWithConditional(
                Pedido.class,
                "user",
                "id",
                iduser,
                "ano",
                "tipoPagamento",
                TipoPagamento.valueOf(paymentType)
        );
    }

    public List<Pedido> getPedidoByUserAndByPaymentTypeWhitPagination(
            String iduser,
            String paymentType,
            int pageNumber,
            int pageSize
    ) {
        return super.getEntitiesByForeignKeyAndWithConditionalWithPagination(
                Pedido.class,
                "user",
                "id",
                iduser,
                "mes",
                "tipoPagamento",
                TipoPagamento.valueOf(paymentType),
                pageNumber,
                pageSize
        );
    }

    public List<Pedido> getPedidoByClientName(int idestabelecimento, String clientName) {
        return super.getByTwoEntitiesAndPropertyNameAndEntityId(
                Pedido.class,
                "user",
                "estabelecimento",
                "nome",
                "idestabelecimento",
                "tipoPagamento",
                clientName,
                idestabelecimento
        );
    }
}
