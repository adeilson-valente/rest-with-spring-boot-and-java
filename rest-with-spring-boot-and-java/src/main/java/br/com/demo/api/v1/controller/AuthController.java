package br.com.demo.api.v1.controller;

import br.com.demo.api.v1.docs.AuthControllerDocs;
import br.com.demo.data.dto.PersonDTO;
import br.com.demo.data.dto.security.AccountCredentialsDTO;
import br.com.demo.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint!")
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    @Override
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDTO credentials) {
        if (credentialsIsInvalid(credentials)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        var token = authService.signIn(credentials);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        return token;
    }

    @PutMapping("/refresh/{username}")
    @Override
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {
        if (parametersAreInvalid(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        var token = authService.refreshToken(username, refreshToken);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        return token;
    }

    @PostMapping(value = "/createUser",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public AccountCredentialsDTO create(@RequestBody AccountCredentialsDTO credentials) {
        return authService.create(credentials);
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }

    private boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
        return credentials == null || StringUtils.isBlank(credentials.getUsername()) || StringUtils.isBlank(credentials.getPassword());
    }
}
