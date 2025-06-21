package br.com.demo.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotFoundExeption extends RuntimeException{
    private  static  final long serialVersionUID = 1L;

    public FileNotFoundExeption(String ex) {
        super(ex);
    }

    public FileNotFoundExeption(String ex, Throwable cause) {
        super(ex, cause);
    }
}
