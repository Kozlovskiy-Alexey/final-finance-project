package by.itacademy.account.advice;

import lombok.ToString;

@ToString
public class ResponseError {
    private String logref = "error";
    private String message;

    public ResponseError(String message) {
        this.message = message;
    }

    public String getLogref() {
        return logref;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
