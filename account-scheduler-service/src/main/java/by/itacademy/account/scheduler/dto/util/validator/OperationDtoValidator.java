package by.itacademy.account.scheduler.dto.util.validator;

import by.itacademy.account.scheduler.advice.ResponseError;
import by.itacademy.account.scheduler.advice.SingleValidateException;
import by.itacademy.account.scheduler.advice.ValidateException;
import by.itacademy.account.scheduler.dto.AccountDto;
import by.itacademy.account.scheduler.dto.CategoryDto;
import by.itacademy.account.scheduler.dto.CurrencyDto;
import by.itacademy.account.scheduler.dto.OperationDto;
import by.itacademy.account.scheduler.dto.util.validator.api.IDtoValidator;
import by.itacademy.account.scheduler.service.RestSchedulerService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class OperationDtoValidator implements IDtoValidator<OperationDto> {

    private final RestSchedulerService restSchedulerService;

    public OperationDtoValidator(RestSchedulerService restSchedulerService) {
        this.restSchedulerService = restSchedulerService;
    }

    @Override
    public void validateDto(OperationDto dto) {
        ValidateException validateException = new ValidateException(new ArrayList<>());

        if (dto.getAccountId().isEmpty()) {
            validateException.addError("account shouldn't be empty");
        }
        if (dto.getDescription().isEmpty()) {
            validateException.addError("description shouldn't be empty");
        }
        if (dto.getCurrencyId().isEmpty()) {
            validateException.addError("currency shouldn't be empty");
        }
        if (dto.getCategoryId().isEmpty()) {
            validateException.addError("category shouldn't be empty");
        }
        validateAccount(dto.getAccountId());
        validateCategory(dto.getCategoryId());
        validateCurrency(dto.getCurrencyId());
        if (!validateException.getResponseErrors().isEmpty()) {
            throw new ValidateException(validateException.getResponseErrors());
        }
    }

    private void validateCategory(String categoryId) {
        if (!validateUuid(categoryId)) {
            throw new SingleValidateException(new ResponseError("format category uuid is not correct"));
        }
        CategoryDto categoryDto = restSchedulerService.getCategory(categoryId);
        if (categoryDto == null) {
            throw new SingleValidateException(new ResponseError("there is no category id in database"));
        }
    }

    private CurrencyDto validateCurrency(String currencyId) {
        if (!validateUuid(currencyId)) {
            throw new SingleValidateException(new ResponseError("format currency uuid is not correct"));
        }
        CurrencyDto currencyDto = restSchedulerService.getCurrency(currencyId);
        if (currencyDto == null) {
            throw new SingleValidateException(new ResponseError("there is no currency id in database"));
        }
        return currencyDto;
    }

    private void validateAccount(String accountId) {
        if (!validateUuid(accountId)) {
            throw new SingleValidateException(new ResponseError("format account uuid is not correct"));
        }
        AccountDto accountDto = restSchedulerService.getAccount(accountId);
        String currency = accountDto.getCurrency();
        CurrencyDto currencyDto = validateCurrency(currency);

        if (!currency.equals(currencyDto.getId())) {
            throw new SingleValidateException(new ResponseError("there is no this currency in this account"));
        }
    }
}
