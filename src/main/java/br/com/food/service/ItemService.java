package br.com.food.service;

import br.com.food.dto.ItemResponseDTO;
import br.com.food.entity.Item;
import br.com.food.entity.Pedido;
import br.com.food.entity.Produto;
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

    public void deleteItemsToPedido(Long idpedido) {
        if (idpedido <= 0) {
            throw new IllegalArgumentException("idpedido inválido ou null");
        }

        List<Item> items = this.repository.getItemByIdPedido(idpedido);

        if (!items.isEmpty()) {
            for (Item item : items) {
                this.repository.delete(item);
            }
        }
    }

    public void updateItemsToPedido(Long idpedido, List<ItemResponseDTO> itemsResponseDTO) {
        if (idpedido <= 0) {
            throw new IllegalArgumentException("idpedido inválido ou null");
        }
        Pedido pedido = new Pedido(idpedido);
        List<Item> itemsToUser = itemsResponseDTO.stream()
            .map(i -> this.mapItemResponseDTOTOItem(i, pedido))
            .collect(Collectors.toList());

        List<Item> itemsBD = this.repository.getItemByIdPedido(idpedido);

        itemsBD.removeIf(item -> itemsToUser.contains(item));
        itemsBD.forEach(this.repository::delete);

//        List<Item> itemsToUpdate = itemsToUser.stream()
//            .filter(item -> {
//                return item != null;
//            })
//            .collect(Collectors.toList());
//
//        itemsToUpdate.forEach(this.repository::update);
        for (Item item : itemsToUser) {
            if (item != null) {
                this.repository.update(item);
            }
        }
    }

    public Item mapItemResponseDTOTOItem(ItemResponseDTO itemResponseDTO, Long idpedido) {
        if (itemResponseDTO.iditem() == null || itemResponseDTO.iditem() <= 0) {
            Item item = new Item(itemResponseDTO, new Produto(itemResponseDTO.idproduto()), new Pedido(idpedido));
            this.repository.save(item);

            return item;
        }
        return new Item(itemResponseDTO);
    }

    public Item mapItemResponseDTOTOItem(ItemResponseDTO itemResponseDTO, Pedido pedido) {
        if (itemResponseDTO.iditem() == null || itemResponseDTO.iditem() <= 0) {
            Item item = new Item(itemResponseDTO, new Produto(itemResponseDTO.idproduto()), pedido);
            this.repository.save(item);

            return item;
        }
        return new Item(itemResponseDTO, pedido);
    }
}
