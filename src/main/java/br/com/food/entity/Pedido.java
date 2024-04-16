package br.com.food.entity;

import br.com.food.dto.PedidoRequestDTO;
import br.com.food.dto.PedidoResponseDTO;
import br.com.food.enuns.PedidoStatus;
import br.com.food.enuns.PedidoType;
import br.com.food.enuns.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @JsonBackReference
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagamento")
    private TipoPagamento tipoPagamento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PedidoStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PedidoType type;

    @OneToMany(mappedBy = "pedido" , fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Item> items;

    public Pedido(PedidoResponseDTO responseDTO, User user, List<Item> itemsList) {
        this.idpedido = responseDTO.idpedido();
        this.data = responseDTO.data();
        this.ano = responseDTO.ano();
        this.mes = responseDTO.mes();
        this.dia = responseDTO.dia();
        this.hora = responseDTO.hora();
        this.total = responseDTO.total();
        this.user = user;
        this.tipoPagamento = responseDTO.tipoPagamento();
        this.status = responseDTO.status();
        this.type = responseDTO.type();
        this.items = itemsList;
        for (Item item : this.items) {
            item.setPedido(this);
        }
    }

    public Pedido(PedidoRequestDTO requestDTO, User user, List<Item> itemList) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.data = new Date();
        this.ano = Integer.toString(localDateTime.getYear());
        this.mes = Integer.toString(localDateTime.getMonthValue());
        this.dia = Integer.toString(localDateTime.getDayOfMonth());
        this.hora = localDateTime.format(formatter);
        this.total = requestDTO.total();
        this.user = user;
        this.tipoPagamento = requestDTO.tipoPagamento();
        this.status = requestDTO.status();
        this.type = requestDTO.type();
        this.items = itemList;
        for (Item item : this.items) {
            item.setPedido(this);
        }
    }

    public Pedido(PedidoResponseDTO responseDTO, User user, List<Item> items, Date data) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.idpedido = responseDTO.idpedido();
        this.data = data;
        this.ano = Integer.toString(localDateTime.getYear());
        this.mes = Integer.toString(localDateTime.getMonthValue());
        this.dia = Integer.toString(localDateTime.getDayOfMonth());
        this.hora = localDateTime.format(formatter);
        this.total = responseDTO.total();
        this.user = user;
        this.tipoPagamento = responseDTO.tipoPagamento();
        this.status = responseDTO.status();
        this.type = responseDTO.type();
        this.items = items;
        for (Item item : this.items) {
            item.setPedido(this);
        }
    }

    public Pedido(Long idpedido) {
        this.idpedido = idpedido;
    }

    public Pedido(PedidoResponseDTO responseDTO, User user) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.idpedido = responseDTO.idpedido();
        this.data = new Date();
        this.ano = Integer.toString(localDateTime.getYear());
        this.mes = Integer.toString(localDateTime.getMonthValue());
        this.dia = Integer.toString(localDateTime.getDayOfMonth());
        this.hora = localDateTime.format(formatter);
        this.total = responseDTO.total();
        this.user = user;
        this.tipoPagamento = responseDTO.tipoPagamento();
        this.status = responseDTO.status();
        this.type = responseDTO.type();
    }

    public Pedido(PedidoRequestDTO requestDTO, PedidoStatus pedidoStatus, User user, List<Item> itemList) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.data = new Date();
        this.ano = Integer.toString(localDateTime.getYear());
        this.mes = Integer.toString(localDateTime.getMonthValue());
        this.dia = Integer.toString(localDateTime.getDayOfMonth());
        this.hora = localDateTime.format(formatter);
        this.total = requestDTO.total();
        this.user = user;
        this.tipoPagamento = requestDTO.tipoPagamento();
        this.status = pedidoStatus;
        this.type = requestDTO.type();
        this.items = itemList;
        for (Item item : this.items) {
            item.setPedido(this);
        }
    }
}
