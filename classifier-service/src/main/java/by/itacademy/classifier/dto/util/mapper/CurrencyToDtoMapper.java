package by.itacademy.classifier.dto.util.mapper;

import by.itacademy.classifier.dto.CurrencyDto;
import by.itacademy.classifier.dto.util.mapper.api.IRepositoryMapper;
import by.itacademy.classifier.entity.Currency;
import org.springframework.stereotype.Component;

@Component
public class CurrencyToDtoMapper implements IRepositoryMapper<Currency, CurrencyDto> {

    private Currency entity;
    private CurrencyDto dto;

    @Override
    public Currency dtoToEntity(CurrencyDto dto) {
        return Currency.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    @Override
    public CurrencyDto entityToDto(Currency entity) {
        return CurrencyDto.builder()
                .id(entity.getId())
                .dtCreate(entity.getDtCreate())
                .dtUpdate(entity.getDtUpdate())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
    }
}
