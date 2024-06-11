package com.vg.exception;


import com.vg.model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Component
public class ApiExceptionHandler {
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<Object> handleMissingRequestParameterException(
      MissingServletRequestParameterException e) {
    ErrorModel errorModel = new ErrorModel();
    errorModel.setErrorMessage(e.getMessage());

    return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected ResponseEntity<Object> handleGenericException(
      Exception e) {
    ErrorModel errorModel = new ErrorModel();
    errorModel.setErrorMessage(e.getMessage());

    return new ResponseEntity<>(errorModel, HttpStatus.INTERNAL_SERVER_ERROR);
  }


}
