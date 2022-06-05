package by.itacademy.mail.advice;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidateException extends IllegalArgumentException {

    private final List<ResponseError> responseErrors;

    public ValidateException(List<ResponseError> responseError) {
        this.responseErrors = responseError;
    }

    public void addError(String descriptionError) {
        responseErrors.add(new ResponseError(descriptionError));
    };

    public List<ResponseError> getResponseErrors() {
        return responseErrors;
    }
}
