package br.com.food.repository;

import br.com.food.entity.Produto;
import br.com.food.enuns.ProductCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProdutoRepository extends GenericRepository {


    public List<Produto> getProductsByCategory(int idestabelecimento, String category) {
        return super.getEntitiesByForeignKeyAndWithConditional(
                Produto.class,
                "estabelecimento",
                "idestabelecimento",
                idestabelecimento,
                "nome",
                "categoria",
                ProductCategory.valueOf(category)
        );
    }

    public List<Produto> getAllProducts(int idestabelecimento) {
        return super.getJoinColumn(
                Produto.class,
                "estabelecimento",
                "idestabelecimento",
                idestabelecimento,
                "nome"
        );
    }
}
