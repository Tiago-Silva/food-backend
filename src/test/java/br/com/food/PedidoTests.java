package br.com.food;

import br.com.food.dto.EstabelecimentoRequestDTO;
import br.com.food.dto.PedidoRequestDTO;
import br.com.food.dto.PedidoResponseDTO;
import br.com.food.dto.UserRequestDTO;
import br.com.food.entity.Item;
import br.com.food.entity.User;
import br.com.food.enuns.TipoPagamento;
import br.com.food.enuns.UserRole;
import br.com.food.enuns.UserType;
import br.com.food.repository.UserRepository;
import br.com.food.service.UserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class PedidoTests {

    @Autowired
    private WebTestClient webTestClient = WebTestClient.bindToServer().build();

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    @Order(1)
    void testCreateEmpressSuccess() {
        var empress = new EstabelecimentoRequestDTO(
                "Hamburgueria",
                "Alonsão burguer",
                "12345678912345",
                "12345678912",
                "Brasil",
                "SP",
                "São Paulo",
                "Jardins",
                "Rua Central",
                "123456789"
        );
        webTestClient
                .post()
                .uri("/empress/save")
                .bodyValue(empress)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(2)
    void testCreatePedidoSuccess() {
        var userRequestDTO = new UserRequestDTO(
                "Cliente 01",
                "Cliente xique",
                "cliente",
                "216523165498",
                "213215641",
                "Rua central",
                "321564789",
                "nao obrigatorio",
                UserType.CLIENT,
                UserRole.USER,
                true,
                true,
                true,
                true,
                1
        );
        this.userService.saveUser(userRequestDTO);

        User user = this.userRepository.getUserByLogin("cliente");

        Item item = new Item();
        item.setDescricao("Pedido teste");
        item.setQuantidade(1);
        item.setIdproduto(1);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        var pedido = new PedidoRequestDTO(
                new BigDecimal("25.00"),
                user.getId(),
                TipoPagamento.DINHEIRO,
                itemList
        );
        webTestClient
                .post()
                .uri("/pedido/save")
                .bodyValue(pedido)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(3)
    void testCreatePedidoFailure() {
        webTestClient
                .post()
                .uri("/pedido/save")
                .bodyValue(new PedidoRequestDTO(
                        null,
                        null,
                        TipoPagamento.DINHEIRO,
                        null
                ))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @Order(4)
    void testGetAllPedidoByIdEstablishment() {
        webTestClient
                .get()
                .uri("/pedido/getPedidos/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(PedidoResponseDTO.class).hasSize(1);
    }

    @Test
    @Order(5)
    void testGetAllPedidoByEstablishmentAndByPaymentType() {
        webTestClient
                .get()
                .uri("/pedido/getPedidos/1/DINHEIRO")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(PedidoResponseDTO.class).hasSize(1);
    }

    @Test
    @Order(6)
    void testGetAllPedidoByUser() {
        User user = this.userRepository.getUserByLogin("cliente");
        System.out.println(user.getId());
        webTestClient
                .get()
                .uri("/pedido/getPedidosByUser/" + user.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(PedidoResponseDTO.class).hasSize(1);
    }

    @Test
    @Order(7)
    void testGetPedidoByUserAndByPaymentType() {
        User user = this.userRepository.getUserByLogin("cliente");
        webTestClient
                .get()
                .uri("/pedido/getPedidosByUser/" + user.getId() + "/DINHEIRO")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(PedidoResponseDTO.class).hasSize(1);
    }

    @Test
    @Order(8)
    void testGetById() {
        webTestClient
                .get()
                .uri("/pedido/getById/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(9)
    void testUpdatePedido() {
        User user = this.userRepository.getUserByLogin("cliente");

        Item item = new Item();
        item.setIditem(1L);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        var pedido = new PedidoResponseDTO(
                1L,
                new Date(),
                "2023",
                "12",
                "13",
                "12:00",
                new BigDecimal("25.00"),
                user.getNome(),
                user.getId(),
                TipoPagamento.PIX,
                itemList
        );
        webTestClient
                .put()
                .uri("/pedido/update")
                .bodyValue(pedido)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(10)
    void testGetPedidoByUserAndByPaymentTypeWithPagination() {
        User user = this.userRepository.getUserByLogin("cliente");
        webTestClient
                .get()
                .uri("/pedido/getPedidosByUser/" + user.getId() + "/PIX/1/10")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(PedidoResponseDTO.class).hasSize(1);
    }
}
