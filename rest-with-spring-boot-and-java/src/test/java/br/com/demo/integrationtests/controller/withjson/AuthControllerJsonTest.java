package br.com.demo.integrationtests.controller.withjson;

import br.com.demo.configs.TestConfigs;
import br.com.demo.integrationtests.dto.AccountCredentialsDTO;
import br.com.demo.integrationtests.dto.TokenDTO;
import br.com.demo.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerJsonTest extends AbstractIntegrationTest {

    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(1)
    void signin() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("Adeilson", "admin123");

        tokenDTO = RestAssured.given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(2)
    void refreshToken() {
        tokenDTO = RestAssured.given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("username", tokenDTO.getUsername())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
}