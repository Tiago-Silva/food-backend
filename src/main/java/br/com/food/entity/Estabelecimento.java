package br.com.food.entity;

import br.com.food.dto.EstabelecimentoRequestDTO;
import br.com.food.dto.EstabelecimentoResponseDTO;
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

    public Estabelecimento(EstabelecimentoRequestDTO requestDTO) {
        this.razaoSocial = requestDTO.razaoSocial();
        this.nomeFantasia = requestDTO.nomeFantasia();
        this.cnpj́ = requestDTO.cnpj́();
        this.cpf = requestDTO.cpf();
        this.pais = requestDTO.pais();
        this.estado = requestDTO.estado();
        this.cidade = requestDTO.cidade();
        this.bairro = requestDTO.bairro();
        this.endereco = requestDTO.endereco();
        this.telefone = requestDTO.telefone();
    }

    public Estabelecimento(int idestabelecimento) {
        this.idestabelecimento = idestabelecimento;
    }

    public Estabelecimento(EstabelecimentoResponseDTO responseDTO) {
        this.idestabelecimento = responseDTO.idestabelecimento();
        this.razaoSocial = responseDTO.razaoSocial();
        this.nomeFantasia = responseDTO.nomeFantasia();
        this.cnpj́ = responseDTO.cnpj́();
        this.cpf = responseDTO.cpf();
        this.pais = responseDTO.pais();
        this.estado = responseDTO.estado();
        this.cidade = responseDTO.cidade();
        this.bairro = responseDTO.bairro();
        this.endereco = responseDTO.endereco();
        this.telefone = responseDTO.telefone();
    }
}
