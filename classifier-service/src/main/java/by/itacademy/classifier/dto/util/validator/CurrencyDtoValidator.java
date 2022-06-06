package by.itacademy.classifier.dto.util.validator;

import by.itacademy.classifier.advice.ValidateException;
import by.itacademy.classifier.dto.CurrencyDto;
import by.itacademy.classifier.entity.Currency;
import by.itacademy.classifier.repository.ICurrencyRepository;
import by.itacademy.classifier.dto.util.validator.api.IDtoValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CurrencyDtoValidator implements IDtoValidator<CurrencyDto> {

    private final ICurrencyRepository currencyRepository;

    public CurrencyDtoValidator(ICurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void validateDto(CurrencyDto dto) {
        ValidateException validateException = new ValidateException(new ArrayList<>());
        if (dto.getTitle().isEmpty()) {
            validateException.addError("title shouldn't be empty");
        }
        if (!dto.getTitle().matches("[A-Z]{3}")) {
            validateException.addError("title should consist of three upper case letters");
        }
        if (dto.getDescription().isEmpty() || dto.getDescription() == null) {
            validateException.addError("description shouldn't be empty or null");
        }
        if (validateCurrency(dto.getTitle())) {
            validateException.addError("currency " + dto.getTitle() + " is already in the database");
        }
        if (!validateException.getResponseErrors().isEmpty()) {
            throw new ValidateException(validateException.getResponseErrors());
        }
    }

    private boolean validateCurrency(String currency) {
        for (Currency c : currencyRepository.findAll()) {
            if (!c.getTitle().equals(currency))
                return false;
        }
        return true;
    }
}
