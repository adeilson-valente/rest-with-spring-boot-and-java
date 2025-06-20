package br.com.demo.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestExeption extends RuntimeException{
    private  static  final long serialVersionUID = 1L;

    public BadRequestExeption() {
        super("Unsupported file extension!");
    }

    public BadRequestExeption(String ex) {
        super(ex);
    }
}
