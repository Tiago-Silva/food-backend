package br.com.food.entity;

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

    private String telefone;

    private String endereco;

    private String cpf;

    private String email;

    @NotNull
    private Boolean isClient;

    @NotNull
    private Boolean isReponsable;

    @NotBlank
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
}
