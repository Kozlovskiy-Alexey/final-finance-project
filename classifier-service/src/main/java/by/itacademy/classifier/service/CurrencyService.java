package by.itacademy.classifier.service;

import by.itacademy.classifier.dto.CurrencyDto;
import by.itacademy.classifier.dto.CurrencyPageDto;
import by.itacademy.classifier.repository.ICurrencyRepository;
import by.itacademy.classifier.entity.Currency;
import by.itacademy.classifier.service.api.ICurrencyService;
import by.itacademy.classifier.dto.util.mapper.CurrencyToDtoMapper;
import by.itacademy.classifier.dto.util.validator.CurrencyDtoValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CurrencyService implements ICurrencyService {

    private final ICurrencyRepository currencyRepository;
    private final CurrencyToDtoMapper mapper;
    private final CurrencyDtoValidator currencyValidator;

    public CurrencyService(ICurrencyRepository currencyRepository, CurrencyToDtoMapper mapper,
                           CurrencyDtoValidator currencyValidator) {
        this.currencyRepository = currencyRepository;
        this.mapper = mapper;
        this.currencyValidator = currencyValidator;
    }

    @Override
    public CurrencyDto addCurrency(CurrencyDto currencyDTO) {
        currencyValidator.validateDto(currencyDTO);

        Currency entity = mapper.dtoToEntity(currencyDTO);
        entity.setId(UUID.randomUUID().toString());
        LocalDateTime dateTime = LocalDateTime.now();
        entity.setDtCreate(dateTime);
        entity.setDtUpdate(dateTime);
        Currency currency = currencyRepository.save(entity);
        return mapper.entityToDto(currency);
    }

    @Override
    public CurrencyPageDto getCurrencyPage(int page, int size) {
        currencyValidator.validatePage(page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Currency> currencies = currencyRepository.findAll(pageable);
        int totalElements = (int) currencies.getTotalElements();
        int totalPages = currencies.getTotalPages();
        boolean isFirst = currencies.isFirst();
        boolean isLast = currencies.isLast();
        List<Currency> content = currencies.getContent();

        List<CurrencyDto> dtoList = content.stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());

        return CurrencyPageDto.builder()
                .number(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .first(isFirst)
                .numberOfElements(content.size())
                .last(isLast)
                .content(dtoList)
                .build();
    }
}
