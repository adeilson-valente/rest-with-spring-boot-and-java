package br.com.demo.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJWTAuthenticationExeption extends AuthenticationException {
    private  static  final long serialVersionUID = 1L;

    public InvalidJWTAuthenticationExeption(String ex) {
        super(ex);
    }
}
