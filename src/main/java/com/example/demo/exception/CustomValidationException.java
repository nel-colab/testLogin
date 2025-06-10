package com.example.demo.exception;


public class CustomValidationException extends RuntimeException {

    private int codigo;

    public CustomValidationException(int codigo, String message) {
        super(message);
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

}
