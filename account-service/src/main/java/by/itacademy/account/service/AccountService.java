package by.itacademy.account.service;

import by.itacademy.account.advice.ResponseError;
import by.itacademy.account.advice.SingleValidateException;
import by.itacademy.account.dto.AccountDto;
import by.itacademy.account.dto.AccountPageDto;
import by.itacademy.account.dto.CurrencyDto;
import by.itacademy.account.repository.IAccountRepository;
import by.itacademy.account.repository.IAccountTypeRepository;
import by.itacademy.account.repository.IBalanceRepository;
import by.itacademy.account.entity.Account;
import by.itacademy.account.entity.AccountType;
import by.itacademy.account.entity.Balance;
import by.itacademy.account.service.api.IAccountService;
import by.itacademy.account.dto.util.mapper.AccountToDtoMapper;
import by.itacademy.account.dto.util.validator.AccountDtoValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AccountService implements IAccountService {

    private final IAccountRepository accountRepository;
    private final IAccountTypeRepository accountTypeRepository;
    private final AccountToDtoMapper accountToDtoMapper;
    private final IBalanceRepository balanceRepository;
    private final RestAccountService restAccountService;
    private final AccountDtoValidator accountDtoValidator;

    public AccountService(IAccountRepository accountRepository, IAccountTypeRepository accountTypeRepository,
                          AccountToDtoMapper accountToDtoMapper, IBalanceRepository balanceRepository,
                          RestAccountService restAccountService, AccountDtoValidator accountDtoValidator) {
        this.accountRepository = accountRepository;
        this.accountTypeRepository = accountTypeRepository;
        this.accountToDtoMapper = accountToDtoMapper;
        this.balanceRepository = balanceRepository;
        this.restAccountService = restAccountService;
        this.accountDtoValidator = accountDtoValidator;
    }

    @Override
    @Transactional
    public AccountDto add(AccountDto accountDto) {
        accountDtoValidator.validateDto(accountDto);

        String currencyId = accountDto.getCurrency();
        CurrencyDto currency = restAccountService.getCurrency(accountDto.getCurrency());
        AccountType accountType = accountTypeRepository.findById(accountDto.getType()).orElse(null);
        AccountDto dto = null;
        Account account = null;
        if (currency != null && accountType != null) {
            Account entity = accountToDtoMapper.dtoToEntity(accountDto);
            entity.setUuid(UUID.randomUUID().toString());
            LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
            entity.setDtCreate(dateTime);
            entity.setDtUpdate(dateTime);
            Balance balance = new Balance(entity.getUuid(), 0, entity);
            entity.setBalance(balance);
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            entity.setLogin(userDetails.getUsername());
            account = accountRepository.save(entity);
            dto = accountToDtoMapper.entityToDto(account);
            dto.setBalance(0);
        }
        return dto;
    }

    @Override
    public AccountPageDto getPage(int pageNumber, int size) {
        accountDtoValidator.validatePage(pageNumber, size);
        Pageable pageable = Pageable.ofSize(size).withPage(pageNumber - 1);
        int totalElements = accountRepository.findAll().size();
        List<Account> content = accountRepository.findAll(pageable).getContent();
        int totalPages = totalElements / size;
        return AccountPageDto.builder()
                .number(pageNumber)
                .size(size)
                .totalPages(totalPages == 0 ? 1 : totalPages)
                .totalElements(totalElements)
                .first(pageNumber == 1)
                .numberOfElements(content.size())
                .last(isLastElement(pageable, totalElements))
                .content(content.stream()
                        .map(accountToDtoMapper::entityToDto)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public AccountDto get(String uuid) {
        if (!accountDtoValidator.validateUuid(uuid)) {
            throw new SingleValidateException(new ResponseError("account uuid is not correct"));
        }

        try {
            Account entity = accountRepository.getById(uuid);
            accountDtoValidator.validateLoginFromToken(uuid);
            return accountToDtoMapper.entityToDto(entity);
        } catch (EntityNotFoundException ex) {
            throw new SingleValidateException(new ResponseError("there is no account id in the database"));
        }
    }

    @Override
    @Transactional
    public AccountDto update(String uuid, long lastUpdate, AccountDto accountDTO) {
        Account entity = null;
        if (!accountDtoValidator.validateUuid(uuid)) {
            throw new SingleValidateException(new ResponseError("account uuid is not correct"));
        }
        try {
            entity = accountRepository.getById(uuid);
            accountDtoValidator.validateLoginFromToken(uuid);
        } catch (EntityNotFoundException ex) {
            throw new SingleValidateException(new ResponseError("there is no account id in the database"));
        }
        accountDtoValidator.validateLongDateTimeFormat(lastUpdate);
        accountDtoValidator.validateDto(accountDTO);

        long time = getMilliSecondFromLocalDateTime(entity.getDtUpdate());

        if (time == lastUpdate) {
            entity.setTitle(accountDTO.getTitle());
            entity.setDescription(accountDTO.getDescription());
            entity.setType(accountTypeRepository.getById(accountDTO.getType()));
            entity.setCurrency(accountDTO.getCurrency());
            entity.setDtUpdate(LocalDateTime.now());
            entity = accountRepository.save(entity);
        } else {
            throw new SingleValidateException(new ResponseError("date of last update does not match"));
        }
        return accountToDtoMapper.entityToDto(entity);
    }
}
