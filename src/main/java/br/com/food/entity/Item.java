package br.com.food.entity;

import br.com.food.enuns.ProductCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

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

    @NotBlank
    @Positive
    private int quantidade;

    @NotBlank
    @Size(max = 4)
    private String descricao;

    @NotNull
    private int idproduto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpedido")
    private Pedido pedido;
}
