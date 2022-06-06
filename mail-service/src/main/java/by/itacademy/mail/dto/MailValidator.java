package by.itacademy.mail.dto;

import by.itacademy.mail.advice.MultipleValidateException;

import java.util.ArrayList;
import java.util.List;

public final class MailValidator {

    public static void validateEmail(List<String> emails) {
        MultipleValidateException exception = new MultipleValidateException(new ArrayList<>());

        String regex = "[a-z0-9]+@[a-z]+\\.[a-z]{2,3}";
        boolean isValidate = true;
        for (String email : emails) {
            isValidate = email.matches(regex);
            if (!isValidate) {
                exception.addError("email " + email + " is not valid");
            }
        }
        if (!isValidate) {
            throw exception;
        }
    }
}
