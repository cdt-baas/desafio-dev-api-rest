package com.baas.contas.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.baas.contas.exception.ContaBloqueadaException;
import com.baas.contas.exception.ContaExistException;
import com.baas.contas.exception.SaldoInsuficienteException;



@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ContaExistException.class)
  protected ResponseEntity<Object> handleException (ContaExistException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse("ContaExistException", "Conta já existente.");

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(SaldoInsuficienteException.class)
  protected ResponseEntity<Object> handleException (SaldoInsuficienteException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse("SaldoInsuficienteException", "Saldo Insuficiente para transação.");

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ContaBloqueadaException.class)
  protected ResponseEntity<Object> handleException (ContaBloqueadaException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse("ContaBloqueadaException", "Conta bloqueada.");

    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

}
