package by.itacademy.classifier.dto.util.validator;

import by.itacademy.classifier.advice.MultipleValidateException;
import by.itacademy.classifier.dto.CurrencyDto;
import by.itacademy.classifier.entity.Currency;
import by.itacademy.classifier.repository.ICurrencyRepository;
import by.itacademy.classifier.dto.util.validator.api.IDtoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
public class CurrencyDtoValidator implements IDtoValidator<CurrencyDto> {

    private final ICurrencyRepository currencyRepository;

    public CurrencyDtoValidator(ICurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void validateDto(CurrencyDto dto) {
        MultipleValidateException multipleValidateException = new MultipleValidateException(new ArrayList<>());
        if (dto.getTitle().isEmpty()) {
            multipleValidateException.addError("Title shouldn't be empty.");
        }
        if (!dto.getTitle().matches("[A-Z]{3}")) {
            multipleValidateException.addError("Title should consist of three upper case letters.");
        }
        if (dto.getDescription().isEmpty() || dto.getDescription() == null) {
            multipleValidateException.addError("Description shouldn't be empty or null.");
        }
        if (!validateCurrency(dto.getTitle())) {
            multipleValidateException.addError("Currency " + dto.getTitle() + " is already in the database.");
        }
        if (!multipleValidateException.getResponseErrors().isEmpty()) {
            log.error("CurrencyDto is not valid.", multipleValidateException);
            throw new MultipleValidateException(multipleValidateException.getResponseErrors());
        }
    }

    private boolean validateCurrency(String currency) {
        for (Currency c : currencyRepository.findAll()) {
            if (c.getTitle().equals(currency)) {
                log.error("CurrencyId " + currency + " is not valid.");
                return false;
            }
        }
        return true;
    }
}
