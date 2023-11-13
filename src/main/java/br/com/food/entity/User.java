package br.com.food.entity;

import br.com.food.dto.UserRequestDTO;
import br.com.food.dto.UserResponseDTO;
import br.com.food.enuns.UserType;
import br.com.food.enuns.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "login")})
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    private String nome;

    @Column(name = "sobre_nome")
    private String sobreNome;

    @Column(name = "login", unique = true)
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String telefone;

    @NotBlank
    private String endereco;

    private String cpf;

    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "account_non_expired")
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired;

    @Column(name = "enabled")
    private Boolean enabled;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "idestabelecimento")
    private Estabelecimento estabelecimento;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Pedido> pedidos;

    public User(UserRequestDTO requestDTO, Estabelecimento estabelecimento) {
        this.nome = requestDTO.nome();
        this.sobreNome = requestDTO.sobreNome();
        this.login = requestDTO.login();
        this.password = requestDTO.password();
        this.telefone = requestDTO.telefone();
        this.endereco = requestDTO.endereco();
        this.cpf = requestDTO.cpf();
        this.email = requestDTO.email();
        this.type = requestDTO.type();
        this.role = requestDTO.role();
        this.accountNonExpired = requestDTO.accountNonExpired();
        this.accountNonLocked = requestDTO.accountNonLocked();
        this.credentialsNonExpired = requestDTO.credentialsNonExpired();
        this.enabled = requestDTO.enabled();
        this.estabelecimento = estabelecimento;
    }

    public User(UserResponseDTO responseDTO, Estabelecimento estabelecimento) {
        this.id = responseDTO.id();
        this.nome = responseDTO.nome();
        this.sobreNome = responseDTO.sobreNome();
        this.login = responseDTO.login();
        this.password = responseDTO.password();
        this.telefone = responseDTO.telefone();
        this.endereco = responseDTO.endereco();
        this.cpf = responseDTO.cpf();
        this.email = responseDTO.email();
        this.type = responseDTO.type();
        this.role = responseDTO.role();
        this.accountNonExpired = responseDTO.accountNonExpired();
        this.accountNonLocked = responseDTO.accountNonLocked();
        this.credentialsNonExpired = responseDTO.credentialsNonExpired();
        this.enabled = responseDTO.enabled();
        this.estabelecimento = estabelecimento;
    }

    public User(String iduser) { this.id = iduser; }
}
