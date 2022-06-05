package by.itacademy.account.dto.util.mapper;

import by.itacademy.account.dto.OperationDto;
import by.itacademy.account.dto.util.mapper.api.IRepositoryMapper;
import by.itacademy.account.entity.Operation;
import org.springframework.stereotype.Component;

@Component
public class OperationToDtoMapper implements IRepositoryMapper<Operation, OperationDto> {

    @Override
    public OperationDto entityToDto(Operation entity) {
        return OperationDto.builder()
                .uuid(entity.getUuid())
                .dtCreate(entity.getDtCreate())
                .dtUpdate(entity.getDtUpdate())
                .date(entity.getDtExecute())
                .description(entity.getDescription())
                .operationCategory(entity.getOperationCategory())
                .operationValue(entity.getOperationValue())
                .currencyUuid(entity.getCurrencyUuid())
                .build();
    }

    @Override
    public Operation dtoToEntity(OperationDto dto) {
        return Operation.builder()
                .dtCreate(dto.getDtCreate())
                .description(dto.getDescription())
                .operationValue(dto.getOperationValue())
                .currencyUuid(dto.getCurrencyUuid())
                .build();
    }
}
