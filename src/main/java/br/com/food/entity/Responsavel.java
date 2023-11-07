package br.com.food.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "responsavel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idresponsavel")
public class Responsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idresponsavel;

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Size(max = 100)
    private String sobreNome;

    @NotBlank
    @Size(max = 100)
    private String cpf;

    @NotBlank
    @Size(max = 100)
    private String telefone;

    @NotBlank
    @Size(max = 100)
    private String email;
}
