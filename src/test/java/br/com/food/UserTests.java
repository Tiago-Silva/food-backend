package br.com.food;

import br.com.food.dto.EstabelecimentoRequestDTO;
import br.com.food.dto.UserRequestDTO;
import br.com.food.dto.UserResponseDTO;
import br.com.food.enuns.UserRole;
import br.com.food.enuns.UserType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class UserTests {

    @Autowired
    private WebTestClient webTestClient = WebTestClient.bindToServer().build();

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
    void testCreateClientSuccess() {
        var empress = new UserRequestDTO(
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
        webTestClient
                .post()
                .uri("/user/save")
                .bodyValue(empress)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(3)
    void testCreateUserSuccess() {
        var empress = new UserRequestDTO(
                "Cliente 01",
                "Cliente xique",
                "user",
                "216523165498",
                "213215641",
                "Rua central",
                "321564789",
                "nao obrigatorio",
                UserType.USER,
                UserRole.ADMIN,
                true,
                true,
                true,
                true,
                1
        );
        webTestClient
                .post()
                .uri("/user/save")
                .bodyValue(empress)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(4)
    void testCreateResponsableSuccess() {
        var empress = new UserRequestDTO(
                "Cliente 01",
                "Cliente xique",
                "responsable",
                "216523165498",
                "213215641",
                "Rua central",
                "321564789",
                "nao obrigatorio",
                UserType.RESPONSABLE,
                UserRole.ADMIN,
                true,
                true,
                true,
                true,
                1
        );
        webTestClient
                .post()
                .uri("/user/save")
                .bodyValue(empress)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(5)
    void testCreateUserFailure() {
        webTestClient
                .post()
                .uri("/user/save")
                .bodyValue(new UserRequestDTO(
                        "",
                        "Cliente xique",
                        "cliente",
                        "216523165498",
                        null,
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
                ))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @Order(6)
    void testGetAllSystemUsersOfEmpress() {
        webTestClient
                .get()
                .uri("/user/getUsers/1/USER")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserResponseDTO.class).hasSize(1);
    }

    @Test
    @Order(7)
    void testGetAllCientsOfEmpress() {
        webTestClient
                .get()
                .uri("/user/getUsers/1/CLIENT")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserResponseDTO.class).hasSize(1);
    }

    @Test
    @Order(8)
    void testGetAllResponsableOfEmpress() {
        webTestClient
                .get()
                .uri("/user/getUsers/1/RESPONSABLE")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserResponseDTO.class).hasSize(1);
    }

    @Test
    @Order(9)
    void testGetAllUerByEmpress() {
        webTestClient
                .get()
                .uri("/user/getUsers/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserResponseDTO.class).hasSize(3);
    }

    @Test
    @Order(10)
    void testGetUserById() {
        webTestClient
                .get()
                .uri("/user/getById/1")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
