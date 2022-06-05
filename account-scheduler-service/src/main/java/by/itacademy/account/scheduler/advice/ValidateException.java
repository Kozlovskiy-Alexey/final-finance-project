package by.itacademy.account.scheduler.advice;


import java.util.List;

public class ValidateException extends IllegalArgumentException {

    private final List<ResponseError> responseErrors;

    public ValidateException(List<ResponseError> responseError) {
        this.responseErrors = responseError;
    }

    public void addError(String descriptionError) {
        responseErrors.add(new ResponseError(descriptionError));
    }

    public List<ResponseError> getResponseErrors() {
        return responseErrors;
    }
}
