package by.itacademy.mail.service.api;

import by.itacademy.mail.dto.RequestDto;

public interface IMailService {

    boolean sendMail(String reportId, RequestDto params);

    boolean sendEmailWithAttachment(String reportId, RequestDto params);
}
