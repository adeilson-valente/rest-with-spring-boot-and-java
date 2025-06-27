package br.com.demo.integrationtests.controller.withxml;

import br.com.demo.configs.TestConfigs;
import br.com.demo.integrationtests.dto.AccountCredentialsDTO;
import br.com.demo.integrationtests.dto.TokenDTO;
import br.com.demo.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerXmlTest extends AbstractIntegrationTest {

    private static TokenDTO tokenDTO;
    private static XmlMapper xmlMapper;

    @BeforeAll
    static void setUp() {
        xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(1)
    void signin() throws JsonProcessingException {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("Adeilson", "admin123");

        var content = RestAssured.given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        tokenDTO = xmlMapper.readValue(content, TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(2)
    void refreshToken() throws JsonProcessingException {
        var content = RestAssured.given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("username", tokenDTO.getUsername())
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        tokenDTO = xmlMapper.readValue(content, TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
}