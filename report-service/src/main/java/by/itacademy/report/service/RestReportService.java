package by.itacademy.report.service;

import by.itacademy.report.advice.ResponseError;
import by.itacademy.report.advice.SingleValidateException;
import by.itacademy.report.advice.ValidateException;
import by.itacademy.report.controller.utils.JwtTokenUtil;
import by.itacademy.report.dto.AccountDto;
import by.itacademy.report.dto.AccountPageDto;
import by.itacademy.report.dto.CategoryDto;
import by.itacademy.report.dto.CategoryPageDto;
import by.itacademy.report.dto.CurrencyDto;
import by.itacademy.report.dto.CurrencyPageDto;
import by.itacademy.report.dto.OperationPageDto;
import by.itacademy.report.service.api.IRestService;
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

import java.util.List;

@Service
public class RestReportService implements IRestService {

    private final static String URL_GET_CURRENCY_PAGE = "http://localhost:8082/api/v1/classifier/currency";
    private final static String URL_GET_CATEGORY_PAGE = "http://localhost:8082/api/v1/classifier/operation/category";
    private final static String URL_GET_ACCOUNT_PAGE = "http://localhost:8081/api/v1/account?page=1&size=100";
    private final static String URL_GET_ACCOUNT = "http://localhost:8081/api/v1/account/{uuid}";
    private final static String URL_GET_OPERATION_PAGE = "http://localhost:8081/api/v1/account/{accountID}/operation?page=1&size=100";
    private final RestTemplate restTemplate;

    public RestReportService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public CurrencyDto getCurrency(String currencyId) {
        CurrencyPageDto currencyPageDto = null;
        try {
            ResponseEntity<CurrencyPageDto> response = restTemplate.exchange(
                    URL_GET_CURRENCY_PAGE,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    CurrencyPageDto.class);
            currencyPageDto = response.getBody();
        } catch (RestClientException ex) {
            throw new SingleValidateException(new ResponseError("there are no currencies in the data base"));
        }
        CurrencyDto currencyDto = currencyPageDto.getContent().stream()
                .filter(e -> e.getId().equals(currencyId))
                .findFirst()
                .orElse(null);
        if (currencyDto == null) {
            throw new SingleValidateException(new ResponseError("there is no currency " + currencyId + "in database"));
        }
        return currencyDto;
    }

    @Override
    public CurrencyPageDto getCurrencyPage() {
        try {
            return restTemplate.exchange(
                    URL_GET_CURRENCY_PAGE,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    CurrencyPageDto.class).getBody();
        } catch (RestClientException ex) {
            throw new SingleValidateException(new ResponseError("there are no currencies in the data base"));
        }
    }

    @Override
    public CategoryDto getCategory(String typeId) {
        CategoryPageDto categoryPageDto = null;
        try {
            ResponseEntity<CategoryPageDto> response = restTemplate.exchange(
                    URL_GET_CATEGORY_PAGE,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    CategoryPageDto.class);
            categoryPageDto = response.getBody();
        } catch (RestClientException ex) {
            throw new ValidateException(List.of(new ResponseError("there are no categories in the database")));
        }
        CategoryDto categoryDto = categoryPageDto.getContent().stream()
                .filter(e -> e.getId().equals(typeId))
                .findFirst()
                .orElse(null);
        if (categoryDto == null) {
            throw new SingleValidateException(new ResponseError("there is no category " + typeId + "in database"));
        }
        return categoryDto;
    }

    @Override
    public CategoryPageDto getCategoryPage() {
        try {
            return restTemplate.exchange(
                    URL_GET_CATEGORY_PAGE,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    CategoryPageDto.class).getBody();
        } catch (RestClientException ex) {
            throw new ValidateException(List.of(new ResponseError("there are no categories in the database")));
        }
    }

    @Override
    public OperationPageDto getOperationPage(String accountId) {
        try {
            return restTemplate.exchange(
                    URL_GET_OPERATION_PAGE,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    OperationPageDto.class, accountId).getBody();
        } catch (RestClientException ex) {
            throw new SingleValidateException(new ResponseError("there are no operations in the database"));
        }
    }

    @Override
    public AccountPageDto getAccountPage() {
        try {
            return restTemplate.exchange(
                    URL_GET_ACCOUNT_PAGE,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    AccountPageDto.class).getBody();
        } catch (RestClientException ex) {
            throw new SingleValidateException(new ResponseError("there are no accounts in the database"));
        }
    }

    @Override
    public AccountDto getAccount(String accountId) {
        try {
            return restTemplate.exchange(
                    URL_GET_ACCOUNT,
                    HttpMethod.GET,
                    getHttpEntityWithJwtToken(),
                    AccountDto.class, accountId).getBody();
        } catch (RestClientException ex) {
            throw new SingleValidateException(new ResponseError("there is no account id " + accountId + " in the database"));
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
