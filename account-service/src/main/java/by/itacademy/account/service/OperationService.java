package by.itacademy.account.service;

import by.itacademy.account.advice.ResponseError;
import by.itacademy.account.advice.SingleValidateException;
import by.itacademy.account.advice.ValidateException;
import by.itacademy.account.dto.CategoryDto;
import by.itacademy.account.dto.CurrencyDto;
import by.itacademy.account.dto.OperationDto;
import by.itacademy.account.dto.OperationPageDto;
import by.itacademy.account.dto.util.validator.AccountDtoValidator;
import by.itacademy.account.repository.IAccountRepository;
import by.itacademy.account.repository.IOperationRepository;
import by.itacademy.account.entity.Account;
import by.itacademy.account.entity.Operation;
import by.itacademy.account.service.api.IOperationService;
import by.itacademy.account.dto.util.mapper.OperationToDtoMapper;
import by.itacademy.account.dto.util.validator.OperationDtoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OperationService implements IOperationService {
    private final IOperationRepository operationRepository;
    private final IAccountRepository accountRepository;
    private final OperationToDtoMapper operationToDtoMapper;
    private final OperationDtoValidator operationDtoValidator;
    private final AccountDtoValidator accountDtoValidator;
    private final RestAccountService restAccountService;

    public OperationService(IOperationRepository operationRepository, IAccountRepository accountRepository,
                            OperationToDtoMapper operationToDtoMapper,
                            OperationDtoValidator operationDtoValidator, AccountDtoValidator accountDtoValidator,
                            RestAccountService restAccountService) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
        this.operationToDtoMapper = operationToDtoMapper;
        this.operationDtoValidator = operationDtoValidator;
        this.accountDtoValidator = accountDtoValidator;
        this.restAccountService = restAccountService;
    }

    @Override
    @Transactional
    public OperationDto add(String accountUuid, OperationDto operationDto) {
        accountDtoValidator.validateLoginFromToken(accountUuid);
        operationDtoValidator.validateUuid(accountUuid);
        operationDtoValidator.validateDto(operationDto);

        Account account = null;
        Operation entity = null;


        Optional<Account> mayBeAccount = accountRepository.findById(accountUuid);
        if (mayBeAccount.isEmpty()) {
            log.error("There is no account with id " + accountUuid + " in database.");
            throw new ValidateException(List.of(new ResponseError("There is no account with id " + accountUuid + " in database.")));
        }
        if (!mayBeAccount.get().getCurrency().equals(operationDto.getCurrencyUuid())) {
            log.error("There is no that currency in that account.");
            throw new ValidateException(List.of(new ResponseError("There is no that currency in that account.")));
        }

        entity = operationToDtoMapper.dtoToEntity(operationDto);
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        entity.setUuid(UUID.randomUUID().toString());
        entity.setAccountUuid(mayBeAccount.get());
        entity.setDtCreate(dateTime);
        entity.setDtUpdate(dateTime);
        entity.setDtExecute(dateTime);
        entity.setOperationCategory(operationDto.getOperationCategory());
        Operation save = operationRepository.save(entity);
        log.info("Operation " + save + " saved");
        return operationToDtoMapper.entityToDto(save);
    }

    @Override
    public OperationPageDto get(String uuid, int pageNumber, int size) {
        accountDtoValidator.validateLoginFromToken(uuid);
        operationDtoValidator.validateUuid(uuid);
        operationDtoValidator.validatePage(pageNumber, size);
        Account account = accountDtoValidator.isAccountExist(uuid);

        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by("dtUpdate").descending());
        Page<Operation> operations = operationRepository.findAllByAccountUuid(account, pageable);
        int totalElements = (int) operations.getTotalElements();
        List<Operation> content = operations.getContent();
        int totalPages = operations.getTotalPages();
        boolean isLast = operations.isLast();

        return OperationPageDto.builder()
                .number(pageNumber)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .first(pageNumber == 0)
                .numberOfElements(content.size())
                .last(isLast)
                .content(content.stream()
                        .map(operationToDtoMapper::entityToDto)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    @Transactional
    public OperationDto update(String accountUuid, String operationUuid, long lastUpdate, OperationDto operationDTO) {
        accountDtoValidator.validateLoginFromToken(accountUuid);
        operationDtoValidator.validateUuid(accountUuid);
        operationDtoValidator.validateUuid(operationUuid);
        operationDtoValidator.validateLongDateTimeFormat(lastUpdate);
        operationDtoValidator.validateDto(operationDTO);

        boolean isPresentAccount = accountRepository.findById(accountUuid).isPresent();
        boolean isPresentOperation = operationRepository.findById(operationUuid).isPresent();

        if (!isPresentAccount) {
            log.error("There is no account with id " + accountUuid + " in the database.");
            throw new ValidateException(List.of(new ResponseError("There is no account with id " + accountUuid + " in the database.")));
        } else if (!isPresentOperation) {
            log.error("There is no operation with id " + operationUuid + " in database.");
            throw new ValidateException(List.of(new ResponseError("There is no operation with id " + operationUuid + " in database.")));
        }

        CategoryDto category = restAccountService.getCategory(operationDTO.getOperationCategory());
        CurrencyDto currency = restAccountService.getCurrency(operationDTO.getCurrencyUuid());

        if (category == null) {
            log.error("There is no category " + operationDTO.getOperationCategory() + " in the database.");
            throw new SingleValidateException(new ResponseError("There is no category " +
                    operationDTO.getOperationCategory() + " in the database."));
        } else if (currency == null) {
            log.error("There is no currency " + operationDTO.getCurrencyUuid() + " in the database.");
            throw new SingleValidateException(new ResponseError("There is no currency " +
                    operationDTO.getCurrencyUuid() + " in the database."));
        }

        Operation operation = operationRepository.findByUuid(operationUuid);
        Account account = operation.getAccountUuid();
        long updateTime = operation.getDtUpdate().toInstant(ZoneOffset.UTC).toEpochMilli();
        if (account.getUuid().equals(accountUuid) && updateTime == lastUpdate) {
            operation.setDtUpdate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
            operation.setDtExecute(operationDTO.getDate());
            operation.setDescription(operationDTO.getDescription());
            operation.setOperationCategory(operationDTO.getOperationCategory());
            operation.setOperationValue(operationDTO.getOperationValue());
            operation.setCurrencyUuid(operationDTO.getCurrencyUuid());
            operation = operationRepository.save(operation);
        }
        log.info("Operation " + operation + " updated.");
        return operationToDtoMapper.entityToDto(operation);
    }

    @Override
    @Transactional
    public void delete(String accountUuid, String uuidOperation, long dtUpdate) {
        accountDtoValidator.validateLoginFromToken(accountUuid);
        operationDtoValidator.validateUuid(accountUuid);
        operationDtoValidator.validateUuid(uuidOperation);
        operationDtoValidator.validateLongDateTimeFormat(dtUpdate);

        Account account = accountDtoValidator.isAccountExist(accountUuid);
        Operation operation = operationDtoValidator.isOperationExist(uuidOperation);

        long updateTime = getMilliSecondFromLocalDateTime(operation.getDtUpdate());
        if (operation.getAccountUuid().getUuid().equals(accountUuid) && updateTime == dtUpdate) {
            operationRepository.delete(operation);
            log.info("Operation " + operation + " deleted");
        } else {
            log.error("Date of last update of the operation does not match.");
            throw new SingleValidateException(new ResponseError("Date of last update of the operation does not match."));
        }
    }
}