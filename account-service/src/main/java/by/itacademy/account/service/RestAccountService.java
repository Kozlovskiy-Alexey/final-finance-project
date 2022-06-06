package by.itacademy.account.service;

import by.itacademy.account.advice.ResponseError;
import by.itacademy.account.advice.SingleValidateException;
import by.itacademy.account.controller.utils.JwtTokenUtil;
import by.itacademy.account.dto.CategoryDto;
import by.itacademy.account.dto.CategoryPageDto;
import by.itacademy.account.dto.CurrencyDto;
import by.itacademy.account.dto.CurrencyPageDto;
import by.itacademy.account.service.api.IRestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RestAccountService implements IRestService {
    private final static String URL_GET_CURRENCY_PAGE = "http://localhost:8082/api/v1/classifier/currency";
    private final static String URL_GET_CATEGORY_PAGE = "http://localhost:8082/api/v1/classifier/operation/category";
    private final RestTemplate restTemplate;

    public RestAccountService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public CurrencyDto getCurrency(String currencyId) {
        try {
            ResponseEntity<CurrencyPageDto> response = restTemplate.exchange(
                    URL_GET_CURRENCY_PAGE,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    CurrencyPageDto.class
            );
            CurrencyPageDto currencyPageDto = response.getBody();
            return currencyPageDto.getContent().stream()
                    .filter(e -> e.getId().equals(currencyId))
                    .findFirst()
                    .orElse(null);
        } catch (RestClientException ex) {
            log.error("Error while getting currencyId " + currencyId + "  from data base.");
            throw new SingleValidateException(new ResponseError("Error while getting currencyId " + currencyId + "  from data base."));
        }
    }

    @Override
    public CategoryDto getCategory(String categoryId) {
        try {
            ResponseEntity<CategoryPageDto> response = restTemplate.exchange(
                    URL_GET_CATEGORY_PAGE,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    CategoryPageDto.class
            );
            CategoryPageDto categoryPageDto = response.getBody();
            CategoryDto categoryDto = categoryPageDto.getContent().stream()
                    .filter(e -> e.getId().equals(categoryId))
                    .findFirst()
                    .orElse(null);
            return categoryDto;
        } catch (RestClientException ex) {
            log.error("Error while getting categoryId " + categoryId + " from the database.");
            throw new SingleValidateException(new ResponseError("Error while getting categoryId " + categoryId + " from the database."));
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
