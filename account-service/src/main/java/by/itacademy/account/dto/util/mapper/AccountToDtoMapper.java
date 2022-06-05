package by.itacademy.account.dto.util.mapper;

import by.itacademy.account.dto.AccountDto;
import by.itacademy.account.dto.util.mapper.api.IRepositoryMapper;
import by.itacademy.account.repository.IAccountTypeRepository;
import by.itacademy.account.entity.Account;
import by.itacademy.account.entity.AccountType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class AccountToDtoMapper implements IRepositoryMapper<Account, AccountDto> {

    private final IAccountTypeRepository accountTypeRepository;

    public AccountToDtoMapper(IAccountTypeRepository accountTypeRepository) {
        this.accountTypeRepository = accountTypeRepository;
    }

    @Override
    public Account dtoToEntity(AccountDto dto) {
        AccountType type = accountTypeRepository.getById(dto.getType());

        return Account.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .type(type)
                .currency(dto.getCurrency())
                .build();
    }
    @Override
    public AccountDto entityToDto(Account entity) {
        return AccountDto.builder()
                .uuid(entity.getUuid())
                .dtCreate(entity.getDtCreate())
                .dtUpdate(entity.getDtUpdate())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .type(entity.getType().getAccountTypeId())
                .currency(entity.getCurrency())
                .balance(entity.getBalance().getBalance())
                .build();
    }
}
