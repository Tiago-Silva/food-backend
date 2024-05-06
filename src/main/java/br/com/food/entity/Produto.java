package br.com.food.entity;

import br.com.food.dto.ProdutoRequestDTO;
import br.com.food.dto.ProdutoResponseDTO;
import br.com.food.enuns.ProductCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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
    @Size(min = 4)
    private String nome;

    @NotBlank
    @Size(min = 4)
    private String descricao;

    @NotNull
    @Positive
    private BigDecimal valor;

    @NotNull
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

    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
    private List<Item> items;

    public Produto(ProdutoRequestDTO requestDTO, Estabelecimento estabelecimento) {
        this.nome = requestDTO.nome();
        this.descricao = requestDTO.descricao();
        this.valor = requestDTO.valor();
        this.categoria = requestDTO.categoria();
        this.status = requestDTO.status();
        this.urlImage = requestDTO.urlImage();
        this.enablePromotions = requestDTO.enablePromotions();
        this.estabelecimento = estabelecimento;
    }

    public Produto(ProdutoResponseDTO responseDTO, Estabelecimento estabelecimento) {
        this.idproduto = responseDTO.idproduto();
        this.nome = responseDTO.nome();
        this.descricao = responseDTO.descricao();
        this.valor = responseDTO.valor();
        this.categoria = responseDTO.categoria();
        this.status = responseDTO.status();
        this.urlImage = responseDTO.urlImage();
        this.enablePromotions = responseDTO.enablePromotions();
        this.estabelecimento = estabelecimento;
    }

    public Produto(int idproduto) {
        this.idproduto = idproduto;
    }
}
