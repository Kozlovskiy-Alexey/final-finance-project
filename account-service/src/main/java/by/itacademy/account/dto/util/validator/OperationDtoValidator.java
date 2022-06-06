package by.itacademy.account.dto.util.validator;

import by.itacademy.account.advice.ResponseError;
import by.itacademy.account.advice.SingleValidateException;
import by.itacademy.account.advice.ValidateException;
import by.itacademy.account.dto.CategoryDto;
import by.itacademy.account.dto.CurrencyDto;
import by.itacademy.account.dto.OperationDto;
import by.itacademy.account.entity.Operation;
import by.itacademy.account.repository.IOperationRepository;
import by.itacademy.account.service.RestAccountService;
import by.itacademy.account.dto.util.validator.api.IDtoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class OperationDtoValidator implements IDtoValidator<OperationDto> {

    private final RestAccountService restAccountService;
    private final IOperationRepository operationRepository;

    public OperationDtoValidator(RestAccountService restAccountService, IOperationRepository operationRepository) {
        this.restAccountService = restAccountService;
        this.operationRepository = operationRepository;
    }

    @Override
    public void validateDto(OperationDto dto) {
        ValidateException validateException = new ValidateException(new ArrayList<>());
        if (dto.getDescription().isEmpty()) {
            validateException.addError("Description shouldn't be empty.");
            log.error("Description shouldn't be empty.");
        }
        if (dto.getOperationCategory().isEmpty()) {
            validateException.addError("Category shouldn't be empty.");
        }
        validateUuid(dto.getOperationCategory());
        if (!validateCategory(dto.getOperationCategory())) {
            validateException.addError("There is no operation category id" + dto.getOperationCategory() +
                    " in database.");
            log.error("There is no operation category id" + dto.getOperationCategory() +
                    " in database.");
        }

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher("" + dto.getOperationValue());
        if (!matcher.find()) {
            validateException.addError("Value is not correct.");
            log.error("Value is not correct");
        }
        if (dto.getCurrencyUuid().isEmpty()) {
            validateException.addError("Currency shouldn't be empty.");
            log.error("Currency shouldn't be empty");
        }
        if (!validateCurrency(dto.getCurrencyUuid())) {
            validateException.addError("Currency " + dto.getCurrencyUuid() + " is not in the database.");
            log.error("Currency " + dto.getCurrencyUuid() + " is not in the database.");
        }
        if (!validateException.getResponseErrors().isEmpty()) {
            log.error(validateException.getResponseErrors().toString());
            throw new ValidateException(validateException.getResponseErrors());
        }
    }

    public Operation isOperationExist(String operationUuid) {
        try {
            return operationRepository.findByUuid(operationUuid);
        } catch (EntityNotFoundException ex) {
            log.error("There is no operation with id " + operationUuid + " in the database.");
            throw new SingleValidateException(new ResponseError("there is no operation with id " + operationUuid +
                    " in the database."));
        }
    }

    private boolean validateCategory(String categoryId) {
        CategoryDto categoryDto = restAccountService.getCategory(categoryId);
        if (categoryDto == null) {
            log.error("There is no category id " + categoryId + " in the data base.");
            throw new SingleValidateException(new ResponseError("There is no category id " + categoryId + " in the data base."));
        }
        return categoryDto.getId().equals(categoryId);
    }

    private boolean validateCurrency(String currencyId) {
        CurrencyDto currencyDto = restAccountService.getCurrency(currencyId);
        if (currencyDto == null) {
            log.error("There is no currency id " + currencyId + " in the data base.");
            throw new SingleValidateException(new ResponseError("There is no currency id " + currencyId + " in the data base."));
        }
        return currencyDto.getId().equals(currencyId);
    }
}
