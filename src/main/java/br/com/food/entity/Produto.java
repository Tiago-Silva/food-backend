package br.com.food.entity;

import br.com.food.enuns.ProductCategory;
import br.com.food.enuns.Promotions;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idproduto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idproduto;

    @NotBlank
    @Size(max = 4)
    private String nome;

    @NotBlank
    @Size(max = 4)
    private String descricao;

    @NotNull
    @Positive
    private BigDecimal valor;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private ProductCategory categoria;

    @NotNull
    private Boolean status;

    @Column(name = "url_image")
    private String urlImage;

    @NotNull
    @Column(name = "enable_promotions")
    private Boolean enablePromotions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idestabelecimento")
    private Estabelecimento estabelecimento;
}
