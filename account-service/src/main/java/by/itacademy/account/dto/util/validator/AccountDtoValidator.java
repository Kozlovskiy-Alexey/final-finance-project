package by.itacademy.account.dto.util.validator;

import by.itacademy.account.advice.ResponseError;
import by.itacademy.account.advice.SingleValidateException;
import by.itacademy.account.advice.ValidateException;
import by.itacademy.account.dto.AccountDto;
import by.itacademy.account.dto.CurrencyDto;
import by.itacademy.account.dto.util.validator.api.IDtoValidator;
import by.itacademy.account.entity.Account;
import by.itacademy.account.repository.IAccountRepository;
import by.itacademy.account.repository.IAccountTypeRepository;
import by.itacademy.account.service.RestAccountService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AccountDtoValidator implements IDtoValidator<AccountDto> {

    private final RestAccountService restAccountService;
    private final IAccountTypeRepository accountTypeRepository;
    private final IAccountRepository accountRepository;

    public AccountDtoValidator(RestAccountService restAccountService, IAccountTypeRepository accountTypeRepository,
                               IAccountRepository accountRepository) {
        this.restAccountService = restAccountService;
        this.accountTypeRepository = accountTypeRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void validateDto(AccountDto dto) {
        ValidateException validateException = new ValidateException(new ArrayList<>());

        if (!validateUuid(dto.getCurrency())) {
            validateException.addError("currency uuid is not correct");
        }
        if (dto.getTitle().isEmpty()) {
            validateException.addError("title shouldn't be empty");
        }
        if (dto.getDescription().isEmpty()) {
            validateException.addError("description shouldn't be empty");
        }
        if (dto.getType().isEmpty()) {
            validateException.addError("type shouldn't be empty");
        } else if (accountTypeRepository.findById(dto.getType()).isEmpty()) {
            validateException.addError("type is not in the database");
        }
        if (!isCurrencyPresentInDataBase(dto.getCurrency())) {
            validateException.addError("currency " + dto.getCurrency() + " is not in the database");
        }

        if (!validateException.getResponseErrors().isEmpty()) {
            throw new ValidateException(validateException.getResponseErrors());
        }
    }

    private boolean isCurrencyPresentInDataBase(String currencyId) {
        CurrencyDto currency = restAccountService.getCurrency(currencyId);
        return currency != null;
    }

    public void validateLoginFromToken(String accountId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = userDetails.getUsername();
        Account account = accountRepository.getById(accountId);
        if (!account.getLogin().equals(login)) {
            throw new SingleValidateException(new ResponseError("login from token does not match uuid login"));
        }
    }
}
