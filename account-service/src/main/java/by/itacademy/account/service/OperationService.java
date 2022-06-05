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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
                            OperationDtoValidator operationDtoValidator, AccountDtoValidator accountDtoValidator, RestAccountService restAccountService) {
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
            throw new ValidateException(List.of(new ResponseError("there is no account with id " + accountUuid + " in database")));
        }
        if (!mayBeAccount.get().getCurrency().equals(operationDto.getCurrencyUuid())) {
            throw new ValidateException(List.of(new ResponseError("there is no that currency in that account")));
        }

        entity = operationToDtoMapper.dtoToEntity(operationDto);
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        entity.setUuid(UUID.randomUUID().toString());
        entity.setAccountUuid(mayBeAccount.get());
        entity.setDtCreate(dateTime);
        entity.setDtUpdate(dateTime);
        entity.setDtExecute(dateTime);
        entity.setOperationCategory(operationDto.getOperationCategory());
        return operationToDtoMapper.entityToDto(operationRepository.save(entity));
    }

    @Override
    public OperationPageDto get(String uuid, int pageNumber, int size) {
        accountDtoValidator.validateLoginFromToken(uuid);
        operationDtoValidator.validateUuid(uuid);
        operationDtoValidator.validatePage(pageNumber, size);
        Pageable pageable = Pageable.ofSize(size).withPage(pageNumber - 1);
        int totalElements = operationRepository.findAll().size();
        List<Operation> content = operationRepository.findAllOperationByAccountId(uuid);
        int totalPages = totalElements / size;
        return OperationPageDto.builder()
                .number(pageNumber)
                .size(size)
                .totalPages(totalPages == 0 ? 1 : totalPages)
                .totalElements(totalElements)
                .first(pageNumber == 1)
                .numberOfElements(content.size())
                .last(isLastElement(pageable, totalElements))
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
            throw new ValidateException(List.of(new ResponseError("there is no account with id " + accountUuid + " in database")));
        } else if (!isPresentOperation) {
            throw new ValidateException(List.of(new ResponseError("there is no operation with id " + operationUuid + " in database")));
        }

        CategoryDto category = restAccountService.getCategory(operationDTO.getOperationCategory());
        CurrencyDto currency = restAccountService.getCurrency(operationDTO.getCurrencyUuid());

        if (category == null) {
            throw new ValidateException(List.of(new ResponseError("there is no category " +
                    operationDTO.getOperationCategory() + " in database")));
        } else if (currency == null) {
            throw new ValidateException(List.of(new ResponseError("there is no currency " +
                    operationDTO.getCurrencyUuid() + " in database")));
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
        return operationToDtoMapper.entityToDto(operation);
    }

    @Override
    @Transactional
    public void delete(String accountUuid, String uuidOperation, long dtUpdate) {
        accountDtoValidator.validateLoginFromToken(accountUuid);
        operationDtoValidator.validateUuid(accountUuid);
        operationDtoValidator.validateUuid(uuidOperation);
        operationDtoValidator.validateLongDateTimeFormat(dtUpdate);

        Account account = accountRepository.findByUuid(accountUuid);
        Operation operation = operationRepository.findByUuid(uuidOperation);

        if (account == null) {
            throw new SingleValidateException(new ResponseError("there is no account with id " + accountUuid +
                    " in database"));
        } else if (operation == null) {
            throw new SingleValidateException(new ResponseError("there is no operation with id " + uuidOperation +
                    " in database"));
        }

        long updateTime = operation.getDtUpdate().toInstant(ZoneOffset.UTC).toEpochMilli();
        if (operation.getAccountUuid().getUuid().equals(accountUuid) && updateTime == dtUpdate) {
            operationRepository.delete(operation);
        } else {
            throw new ValidateException(List.of(new ResponseError("date of last update of the operation does not match")));
        }
    }
}