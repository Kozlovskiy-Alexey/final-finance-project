package by.itacademy.account.scheduler.service;

import by.itacademy.account.scheduler.advice.ResponseError;
import by.itacademy.account.scheduler.advice.SingleValidateException;
import by.itacademy.account.scheduler.controller.utils.JwtTokenUtil;
import by.itacademy.account.scheduler.dto.AccountDto;
import by.itacademy.account.scheduler.dto.CategoryDto;
import by.itacademy.account.scheduler.dto.CategoryPageDto;
import by.itacademy.account.scheduler.dto.CurrencyDto;
import by.itacademy.account.scheduler.dto.CurrencyPageDto;
import by.itacademy.account.scheduler.dto.OperationDto;
import by.itacademy.account.scheduler.service.api.IRestService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class RestSchedulerService implements IRestService {

    private final static String URL_GET_CURRENCY_PAGE = "http://localhost:8082/api/v1/classifier/currency";
    private final static String URL_GET_CATEGORY_PAGE = "http://localhost:8082/api/v1/classifier/operation/category";
    private final static String URL_GET_ACCOUNT = "http://localhost:8081/api/v1/account/";
    private final static String URL_POST_OPERATION = "http://localhost:8081/api/v1/account/{accountId}/operation";
    private final RestTemplate restTemplate;

    public RestSchedulerService(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
    }

    @Override
    public CurrencyDto getCurrency(String currencyId) {
        try {
            ResponseEntity<CurrencyPageDto> response = restTemplate.exchange(
                    URL_GET_CURRENCY_PAGE,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    CurrencyPageDto.class);
            CurrencyDto currencyDto = response.getBody().getContent().stream()
                    .filter(e -> e.getId().equals(currencyId))
                    .findFirst()
                    .orElse(null);
            if (currencyDto == null) {
                throw new SingleValidateException(new ResponseError("there is no currency " + currencyId + " in the database"));
            }
            return currencyDto;
        } catch (RestClientException ex) {
            throw new SingleValidateException(new ResponseError("Error while getting currency from data base."));
        }
    }

    @Override
    public CategoryDto getCategory(String typeId) {
        CategoryDto categoryDto = null;
        try {
            ResponseEntity<CategoryPageDto> response = restTemplate.exchange(
                    URL_GET_CATEGORY_PAGE,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    CategoryPageDto.class);
            categoryDto = response.getBody().getContent().stream()
                    .filter(e -> e.getId().equals(typeId))
                    .findFirst()
                    .orElse(null);
            if (categoryDto == null) {
                throw new SingleValidateException(new ResponseError("there is no category id " + typeId + " in the database"));
            }
            return categoryDto;
        } catch (RestClientException ex) {
            throw new SingleValidateException(new ResponseError("Error while getting category from data base."));
        }
    }

    @Override
    public AccountDto getAccount(String accountId) {
        try {
            ResponseEntity<AccountDto> response = restTemplate.exchange(URL_GET_ACCOUNT + accountId,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    AccountDto.class);
            return response.getBody();
        } catch (RestClientException ex) {
            throw new SingleValidateException(new ResponseError("error while gr " + accountId + " in the data base"));
        }
    }

    @Override
    public void putOperation(OperationDto operationDto, String userLogin) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String token = JwtTokenUtil.generateAccessTokenFromUserLogin(userLogin);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<OperationDto> entity = new HttpEntity<>(operationDto, headers);
        restTemplate.postForObject(
                URL_POST_OPERATION,
                entity,
                OperationDto.class,
                operationDto.getAccountId());
    }

    private HttpEntity<String> getHttpEntityWithJwtToken() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = JwtTokenUtil.generateAccessToken(userDetails);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }
}
