package by.itacademy.mail.dto;

import java.util.List;

public class MailValidatorRunner {
    public static void main(String[] args) {

        List<String> emails = List.of("alex@mail.ru",
                "nate@gmail.com",
                "ilia@a.v");

        MailValidator.validateEmail(emails);
    }
}
