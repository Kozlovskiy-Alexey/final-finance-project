package by.itacademy.report.advice;

public class SingleValidateException extends IllegalArgumentException {
    private final ResponseError responseError;

    public SingleValidateException(ResponseError responseError) {
        this.responseError = responseError;
    }

    public void addError(String descriptionError) {
        responseError.setMessage(descriptionError);
    }

    public ResponseError getResponseError() {
        return responseError;
    }
}

