package br.com.food;

import br.com.food.dto.EstabelecimentoRequestDTO;
import br.com.food.dto.EstabelecimentoResponseDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class EstabelecimentoTests {
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
    void testCreateEmpressFailure() {
        webTestClient
                .post()
                .uri("/empress/save")
                .bodyValue(new EstabelecimentoRequestDTO(
                        "",
                        "Alonsão burguer",
                        "",
                        "12345678912",
                        "Brasil",
                        "SP",
                        "São Paulo",
                        "Jardins",
                        "Rua Central",
                        "123456789"
                ))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @Order(3)
    void testGetAllEmpress() {
        webTestClient
                .get()
                .uri("/empress/getAll")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(EstabelecimentoResponseDTO.class).hasSize(1);
    }


    @Test
    @Order(4)
    void testGetEmpressById() {
        webTestClient
                .get()
                .uri("/empress/getById/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(EstabelecimentoResponseDTO.class);
    }

}
