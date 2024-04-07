package br.com.food.entity;

import br.com.food.dto.ItemRequestDTO;
import br.com.food.dto.ItemResponseDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "iditem")
public class Item implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iditem;

    @NotNull
    @Positive
    private int quantidade;

    @NotBlank
    @Size(min = 4)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idproduto")
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpedido")
    private Pedido pedido;


    public Item(ItemRequestDTO itemRequestDTO) {
        this.quantidade = itemRequestDTO.quantidade();
        this.descricao = itemRequestDTO.descricao();
        this.produto = new Produto(itemRequestDTO.idproduto());
    }

    public Item(ItemResponseDTO itemResponseDTO) {
        this.iditem = itemResponseDTO.iditem();
        this.quantidade = itemResponseDTO.quantidade();
        this.descricao = itemResponseDTO.descricao();
        this.produto = new Produto(itemResponseDTO.idproduto());
    }

    public Item(ItemResponseDTO itemResponseDTO, Produto produto, Pedido pedido) {
        this.iditem = itemResponseDTO.iditem();
        this.quantidade = itemResponseDTO.quantidade();
        this.descricao = itemResponseDTO.descricao();
        this.produto = produto;
        this.pedido = pedido;
    }

    public Item(ItemResponseDTO itemResponseDTO, Pedido pedido) {
        this.iditem = itemResponseDTO.iditem();
        this.quantidade = itemResponseDTO.quantidade();
        this.descricao = itemResponseDTO.descricao();
        this.produto = new Produto(itemResponseDTO.idproduto());
        this.pedido = pedido;
    }
}
