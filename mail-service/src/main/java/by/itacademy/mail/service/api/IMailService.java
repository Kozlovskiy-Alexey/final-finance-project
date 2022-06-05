package by.itacademy.mail.service.api;

import java.util.Map;

public interface IMailService {

    boolean sendMail(String reportId, Map<String, String> params);

    boolean sendEmailWithAttachment(String reportId, Map<String, String> params);
}
