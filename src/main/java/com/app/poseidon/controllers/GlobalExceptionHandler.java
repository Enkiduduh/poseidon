package com.app.poseidon.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Renvoie sur la page 404 si la page n'existe pas
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404() {
        return "error/404";
    }

    // Renvoie sur la page 403 si access denied
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle403() {
        return "error/403";
    }
}
