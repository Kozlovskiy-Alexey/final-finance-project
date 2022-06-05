package by.itacademy.mail.service;

import by.itacademy.mail.advice.ResponseError;
import by.itacademy.mail.advice.SingleValidateException;
import by.itacademy.mail.service.api.IMailService;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.util.Map;

@Service
public class MailService implements IMailService {

    private final JavaMailSender mailSender;
    private final RestMailService restMailService;

    public MailService(JavaMailSender mailSender, RestMailService restMailService) {
        this.mailSender = mailSender;
        this.restMailService = restMailService;
    }

    @Override
    public boolean sendMail(String reportId, Map<String, String> params) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("simbamailhelper@mail.ru");
        message.setTo(params.get("email"));
        message.setSubject("Sending report test email");
        message.setText("Hello, your request has been processed, the report " + reportId +
                " has been sent as an attachment.");

        try {
            this.mailSender.send(message);
        } catch (MailException ex) {
            throw new SingleValidateException(new ResponseError("an error occurred while sending email"));
        }
        return true;
    }

    @Override
    public boolean sendEmailWithAttachment(String reportId, Map<String, String> params) {
        byte[] report = restMailService.getReport(reportId);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(params.get("email"));
            helper.setSubject("Test email with an attachment");
            helper.setText("Hello, your request has been processed, the report " + reportId + " has been sent as an " +
                    "attachment.");
            ByteArrayDataSource bads = new ByteArrayDataSource(report,"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            helper.addAttachment("report.xlsx", bads);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return true;
    }
}
