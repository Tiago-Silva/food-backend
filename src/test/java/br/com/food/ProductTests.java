package br.com.food;

import br.com.food.dto.EstabelecimentoRequestDTO;
import br.com.food.dto.ProdutoRequestDTO;
import br.com.food.dto.UserRequestDTO;
import br.com.food.dto.UserResponseDTO;
import br.com.food.enuns.ProductCategory;
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

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class ProductTests {

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
    void testCreateProductSuccess() {
        var empress = new ProdutoRequestDTO(
              "product name",
              "product test",
              new BigDecimal(25.00),
                ProductCategory.ARTESANAIS,
                true,
                "url image",
                false,
                1
        );
        webTestClient
                .post()
                .uri("/produto/save")
                .bodyValue(empress)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(3)
    void testCreateProductFailure() {
        webTestClient
                .post()
                .uri("/produto/save")
                .bodyValue(new ProdutoRequestDTO(
                        "",
                        "product test",
                        null,
                        ProductCategory.ARTESANAIS,
                        true,
                        "url image",
                        false,
                        1
                ))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @Order(6)
    void testGetProductsByCategory() {
        webTestClient
                .get()
                .uri("/produto/getProdutos/1/ARTESANAIS")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserResponseDTO.class).hasSize(1);
    }

    @Test
    @Order(4)
    void testGetAllProductsByEstablishment() {
        webTestClient
                .get()
                .uri("/produto/getProdutos/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserResponseDTO.class).hasSize(1);
    }

    @Test
    @Order(5)
    void testGetById() {
        webTestClient
                .get()
                .uri("/produto/getById/1")
                .exchange()
                .expectStatus().isOk();
    }
}
