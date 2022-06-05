package by.itacademy.mail.service;

import by.itacademy.mail.advice.ResponseError;
import by.itacademy.mail.advice.SingleValidateException;
import by.itacademy.mail.controller.utils.JwtTokenUtil;
import by.itacademy.mail.service.api.IRestMailService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestMailService implements IRestMailService {

    private final static String URL_GET_REPORT = "http://localhost:8084/api/v1/report/{reportId}/export";

    private final RestTemplate restTemplate;

    public RestMailService(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }

    @Override
    public byte[] getReport(String reportId) {
        try {
            return restTemplate.exchange(URL_GET_REPORT,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    byte[].class, reportId).getBody();
        } catch (RestClientException ex) {
            throw new SingleValidateException(new ResponseError("there is no report with id " + reportId));
        }
    }

    private HttpEntity<String> getHttpEntityWithJwtToken() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = JwtTokenUtil.generateAccessToken(userDetails);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }
}
