package by.itacademy.account.scheduler.dto.util.mapper;


import by.itacademy.account.scheduler.dto.OperationDto;
import by.itacademy.account.scheduler.dto.util.mapper.api.IRepositoryMapper;
import by.itacademy.account.scheduler.repository.entity.Operation;
import org.springframework.stereotype.Component;

@Component
public class OperationToDtoMapper implements IRepositoryMapper<Operation, OperationDto> {

    @Override
    public OperationDto entityToDto(Operation entity) {
        return OperationDto.builder()
                .accountId(entity.getAccountId())
                .description(entity.getDescription())
                .value(entity.getValue())
                .currencyId(entity.getCurrencyID())
                .categoryId(entity.getCategory())
                .build();
    }
    public Operation dtoToEntity(OperationDto dto) {
        return null;
    }
}
