package by.itacademy.mail.controller;

import by.itacademy.mail.dto.RequestDto;
import by.itacademy.mail.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/api/v1/mail/{uuid}")
    public ResponseEntity<String> sendEmail(@PathVariable(name = "uuid") String reportId,
                                            @RequestBody RequestDto params) {
        mailService.sendEmailWithAttachment(reportId, params);
        return ResponseEntity.ok("report sent");
    }


}
