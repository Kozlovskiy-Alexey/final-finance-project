package by.itacademy.account.dto.util.validator;

import by.itacademy.account.advice.ValidateException;
import by.itacademy.account.dto.CategoryDto;
import by.itacademy.account.dto.CurrencyDto;
import by.itacademy.account.dto.OperationDto;
import by.itacademy.account.service.RestAccountService;
import by.itacademy.account.dto.util.validator.api.IDtoValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class OperationDtoValidator implements IDtoValidator<OperationDto> {

    private final RestAccountService restAccountService;

    public OperationDtoValidator(RestAccountService restAccountService) {
        this.restAccountService = restAccountService;
    }

    @Override
    public void validateDto(OperationDto dto) {
        ValidateException validateException = new ValidateException(new ArrayList<>());
        if (dto.getDescription().isEmpty()) {
            validateException.addError("description shouldn't be empty");
        }
        if (dto.getOperationCategory().isEmpty()) {
            validateException.addError("category shouldn't be empty");
        }
        validateUuid(dto.getOperationCategory());
        if (!validateCategory(dto.getOperationCategory())) {
            validateException.addError("there is no operation category id" + dto.getOperationCategory() +
                    " in database");
        }

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher("" + dto.getOperationValue());
        if (!matcher.find()) {
            validateException.addError("value is not correct");
        }
        if (dto.getCurrencyUuid().isEmpty()) {
            validateException.addError("currency shouldn't be empty");
        }
        if (!validateCurrency(dto.getCurrencyUuid())) {
            validateException.addError("currency " + dto.getCurrencyUuid() + " is not in the database");
        }
        if (!validateException.getResponseErrors().isEmpty()) {
            throw new ValidateException(validateException.getResponseErrors());
        }
    }

    private boolean validateCategory(String categoryId) {
        CategoryDto categoryDto = restAccountService.getCategory(categoryId);
        return categoryDto.getId().equals(categoryId);
    }

    private boolean validateCurrency(String currencyId) {
        CurrencyDto currencyDto = restAccountService.getCurrency(currencyId);
        return currencyDto.getId().equals(currencyId);
    }
}
