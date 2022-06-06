package by.itacademy.classifier.service;

import by.itacademy.classifier.dto.CategoryDto;
import by.itacademy.classifier.dto.CategoryPageDto;
import by.itacademy.classifier.repository.ICategoryRepository;
import by.itacademy.classifier.entity.Category;
import by.itacademy.classifier.service.api.ICategoryService;
import by.itacademy.classifier.dto.util.mapper.CategoryToDtoMapper;
import by.itacademy.classifier.dto.util.validator.CategoryDtoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final CategoryToDtoMapper mapper;
    private final CategoryDtoValidator categoryDtoValidator;

    public CategoryService(ICategoryRepository categoryRepository, CategoryToDtoMapper mapper,
                           CategoryDtoValidator categoryDtoValidator) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.categoryDtoValidator = categoryDtoValidator;
    }

    @Override
    public CategoryDto add(CategoryDto categoryDto) {
        categoryDtoValidator.validateDto(categoryDto);

        Category entity = mapper.dtoToEntity(categoryDto);
        entity.setId(UUID.randomUUID().toString());
        LocalDateTime dateTime = LocalDateTime.now();
        entity.setDtCreate(dateTime);
        entity.setDtUpdate(dateTime);
        Category savedCategory = categoryRepository.save(entity);
        log.info("NEW Category " + entity + " saved.");
        return mapper.entityToDto(savedCategory);
    }

    @Override
    public CategoryPageDto getCategoryPage(int page, int size) {
        categoryDtoValidator.validatePage(page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryRepository.findAll(pageable);
        int totalElements = (int) categories.getTotalElements();
        int totalPages = categories.getTotalPages();
        boolean isFirst = categories.isFirst();
        boolean isLast = categories.isLast();
        List<Category> content = categories.getContent();

        List<CategoryDto> dtoList = content.stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());

        return CategoryPageDto.builder()
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
