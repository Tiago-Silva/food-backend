package br.com.food.entity;

import br.com.food.dto.RegisterDTO;
import br.com.food.dto.UserRequestDTO;
import br.com.food.dto.UserResponseDTO;
import br.com.food.enuns.UserType;
import br.com.food.enuns.UserRole;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "Por favor, forneça um número de telefone válido no formato (XX) XXXXX-XXXX.")
    private String telefone;

    private String pais;

    private String estado;

    private String cidade;

    private String cep;

    @NotBlank
    private String endereco;

    private String cpf;

    @Email(message = "Por favor, forneça um endereço de e-mail válido.")
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
    @JsonManagedReference
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

    public User(RegisterDTO data, String encryptedPassword) {
        this.nome = data.nome();
        this.sobreNome = data.sobreNome();
        this.login = data.email();
        this.password = encryptedPassword;
        this.telefone = data.telefone();
        this.pais = "Brasil";
        this.estado = "Bahia";
        this.cidade = "Itambé";
        this.cep = "45140-000";
        this.endereco = data.endereco();
        this.email = data.email();
        this.type = data.type();
        this.role = UserRole.fromUserType(data.type());
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.estabelecimento = new Estabelecimento(data.idestabelecimento());
    }

    public User(String id, RegisterDTO data, String encryptedPassword) {
        this.id = id;
        this.nome = data.nome();
        this.sobreNome = data.sobreNome();
        this.login = data.email();
        this.password = encryptedPassword;
        this.telefone = data.telefone();
        this.pais = "Brasil";
        this.estado = "Bahia";
        this.cidade = "Itambé";
        this.cep = "45140-000";
        this.endereco = data.endereco();
        this.email = data.email();
        this.type = data.type();
        this.role = UserRole.fromUserType(data.type());
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
        this.estabelecimento = new Estabelecimento(data.idestabelecimento());
    }
}
