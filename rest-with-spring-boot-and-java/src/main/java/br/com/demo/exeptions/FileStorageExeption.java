package br.com.demo.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileStorageExeption extends RuntimeException{
    private  static  final long serialVersionUID = 1L;

    public FileStorageExeption(String ex) {
        super(ex);
    }

    public FileStorageExeption(String ex, Throwable cause) {
        super(ex, cause);
    }
}
