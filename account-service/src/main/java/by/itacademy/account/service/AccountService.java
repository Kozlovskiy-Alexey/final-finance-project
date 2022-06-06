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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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
            log.info("Account " + dto + " saved.");
        }
        return dto;
    }

    @Override
    public AccountPageDto getPage(int pageNumber, int size) {
        accountDtoValidator.validatePage(pageNumber, size);
        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by("dtUpdate").descending());
        Page<Account> accounts = accountRepository.findAll(pageable);
        List<Account> content = accounts.getContent();
        int totalElements = (int) accounts.getTotalElements();
        int totalPages = accounts.getTotalPages();
        boolean isLast = accounts.isLast();
        boolean isFirst = accounts.isFirst();

        return AccountPageDto.builder()
                .number(pageNumber)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .first(isFirst)
                .numberOfElements(content.size())
                .last(isLast)
                .content(content.stream()
                        .map(accountToDtoMapper::entityToDto)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public AccountDto get(String uuid) {
        if (!accountDtoValidator.validateUuid(uuid)) {
            log.error("Account uuid is not correct.");
            throw new SingleValidateException(new ResponseError("account uuid is not correct"));
        }
        Account entity = accountDtoValidator.isAccountExist(uuid);
        accountDtoValidator.validateLoginFromToken(uuid);
        return accountToDtoMapper.entityToDto(entity);
    }

    @Override
    @Transactional
    public AccountDto update(String uuid, long lastUpdate, AccountDto accountDTO) {
        if (!accountDtoValidator.validateUuid(uuid)) {
            log.error("Account uuid is not correct.");
            throw new SingleValidateException(new ResponseError("Account uuid is not correct."));
        }
        accountDtoValidator.validateDto(accountDTO);
        Account entity = accountDtoValidator.isAccountExist(uuid);
        accountDtoValidator.validateLoginFromToken(uuid);
        accountDtoValidator.validateLongDateTimeFormat(lastUpdate);

        long time = getMilliSecondFromLocalDateTime(entity.getDtUpdate());
        if (time == lastUpdate) {
            entity.setTitle(accountDTO.getTitle());
            entity.setDescription(accountDTO.getDescription());
            entity.setType(accountTypeRepository.getById(accountDTO.getType()));
            entity.setCurrency(accountDTO.getCurrency());
            entity.setDtUpdate(LocalDateTime.now());
            entity = accountRepository.save(entity);
        } else {
            log.error("Date of last update does not match.");
            throw new SingleValidateException(new ResponseError("Date of last update does not match."));
        }
        log.info("Account " + entity + " updated.");
        return accountToDtoMapper.entityToDto(entity);
    }
}
