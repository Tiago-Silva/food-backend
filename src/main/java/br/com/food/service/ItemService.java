package br.com.food.service;

import br.com.food.dto.ItemResponseDTO;
import br.com.food.entity.Item;
import br.com.food.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository repository;
    public ItemService(ItemRepository repository) {this.repository = repository;}

    public List<ItemResponseDTO> getItemsByIdPedido (Long idpedido) {
        if (idpedido <= 0) {
            throw new IllegalArgumentException("idpedido inválido ou null");
        }

        return this.repository.getItemByIdPedido(idpedido)
                .stream().map(this::mapItemToResponseDTO).collect(Collectors.toList());
    }

    public ItemResponseDTO mapItemToResponseDTO(Item item) {
        return new ItemResponseDTO(
                item.getIditem(),
                item.getQuantidade(),
                item.getDescricao(),
                item.getProduto().getValor(),
                item.getProduto().getValor().multiply(BigDecimal.valueOf(item.getQuantidade())),
                item.getProduto().getIdproduto(),
                item.getPedido().getIdpedido()
        );
    }
}
