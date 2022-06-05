package by.itacademy.account.scheduler.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseError> badRequestHandler(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(new ResponseError("Запрос содержит некорретные данные. Измените запрос и отправьте " +
                "его ещё раз"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> badRequestHandler(NullPointerException ex) {
        return new ResponseEntity<>(new ResponseError("Запрос содержит некорретные данные. Измените запрос и отправьте " +
                "его ещё раз"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<List<ResponseError>> badJsonParameter(ValidateException ex) {
        return new ResponseEntity<>(ex.getResponseErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> badArgument(SingleValidateException ex) {
        return new ResponseEntity<>(ex.getResponseError(), HttpStatus.BAD_REQUEST);
    }

}
