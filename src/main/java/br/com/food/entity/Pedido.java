package br.com.food.entity;

import br.com.food.dto.PedidoRequestDTO;
import br.com.food.dto.PedidoResponseDTO;
import br.com.food.enuns.TipoPagamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idpedido")
public class Pedido implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idpedido;

    @Temporal(TemporalType.DATE)
    private Date data;

    private String ano;

    private String mes;

    private String dia;

    private String hora;

    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagamento")
    private TipoPagamento tipoPagamento;

    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Item> items;

    public Pedido(PedidoRequestDTO requestDTO, User user) {
        LocalDateTime localDateTime = LocalDateTime.now();
        this.data = new Date();
        this.ano = Integer.toString(localDateTime.getYear());
        this.mes = Integer.toString(localDateTime.getMonthValue());
        this.dia = Integer.toString(localDateTime.getDayOfMonth());
        this.hora = Integer.toString(localDateTime.getHour());
        this.total = requestDTO.total();
        this.user = user;
        this.tipoPagamento = requestDTO.tipoPagamento();
        this.items = requestDTO.items();
        for (Item item : this.items) {
            item.setPedido(this);
        }
    }

    public Pedido(PedidoResponseDTO responseDTO, User user) {
        this.idpedido = responseDTO.idpedido();
        this.data = responseDTO.data();
        this.ano = responseDTO.ano();
        this.mes = responseDTO.mes();
        this.dia = responseDTO.dia();
        this.hora = responseDTO.hora();
        this.total = responseDTO.total();
        this.user = user;
        this.tipoPagamento = responseDTO.tipoPagamento();
        this.items = responseDTO.items();
        for (Item item : this.items) {
            item.setPedido(this);
        }
    }
}
