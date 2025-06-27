package br.com.demo.integrationtests.controller.withyml;

import br.com.demo.configs.TestConfigs;
import br.com.demo.integrationtests.controller.withyml.mapper.YAMLMapper;
import br.com.demo.integrationtests.dto.AccountCredentialsDTO;
import br.com.demo.integrationtests.dto.TokenDTO;
import br.com.demo.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerYmlTest extends AbstractIntegrationTest {

    private static TokenDTO tokenDTO;
    private static YAMLMapper yamlMapper;

    @BeforeAll
    static void setUp() {
        yamlMapper = new YAMLMapper();

        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(1)
    void signin() throws JsonProcessingException {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("Adeilson", "admin123");

        tokenDTO = RestAssured.given()
                .config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig().
                                                encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(credentials, yamlMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class, yamlMapper);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(2)
    void refreshToken() throws JsonProcessingException {
        tokenDTO = RestAssured.given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig().
                                                encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("username", tokenDTO.getUsername())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class, yamlMapper);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
}