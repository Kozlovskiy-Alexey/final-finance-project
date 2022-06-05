package by.itacademy.user.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseError> notValidPassword(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ResponseError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
