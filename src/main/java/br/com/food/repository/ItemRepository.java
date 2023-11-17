package br.com.food.repository;

import br.com.food.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepository extends GenericRepository {

    public List<Item> getItemByIdPedido(Long idpedido) {
        return super.getJoinColumn(
                Item.class,
                "pedido",
                "idpedido",
                idpedido,
                "descricao"
        );
    }
}
