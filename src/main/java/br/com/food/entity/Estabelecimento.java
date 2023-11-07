package br.com.food.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "estabelecimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idestabelecimento")
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idestabelecimento;

    @NotBlank
    @Size(max = 100)
    private String razaoSocial;

    @NotBlank
    @Size(max = 100)
    private String nomeFantasia;

    @Size(max = 15)
    private String cnpj́;

    @Size(max = 15)
    private String cpf;

    @NotBlank
    @Size(max = 30)
    private String pais;

    @NotBlank
    @Size(max = 20)
    private String estado;

    @NotBlank
    @Size(max = 100)
    private String cidade;

    @NotBlank
    @Size(max = 100)
    private String bairro;

    @NotBlank
    @Size(max = 100)
    private String endereco;

    @NotBlank
    @Size(max = 100)
    private String telefone;

    @OneToMany(mappedBy="estabelecimento", fetch = FetchType.LAZY)
    private List<User> usuarios;

    @OneToMany(mappedBy = "estabelecimento", fetch = FetchType.LAZY)
    private List<Produto> produtos;
}
