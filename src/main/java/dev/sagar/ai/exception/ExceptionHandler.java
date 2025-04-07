package dev.sagar.ai.exception;

import dev.sagar.ai.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

  @org.springframework.web.bind.annotation.ExceptionHandler(InvalidParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleInvalidParameterException(InvalidParameterException ex) {
    return new ErrorDTO("invalid_parameter", ex.getMessage());
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorDTO handleGlobalException(Exception ex) {
    return new ErrorDTO("server_error", ex.getMessage());
  }
}
