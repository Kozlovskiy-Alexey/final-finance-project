package by.itacademy.classifier.service;

import by.itacademy.classifier.dto.CategoryDto;
import by.itacademy.classifier.dto.CategoryPageDto;
import by.itacademy.classifier.repository.ICategoryRepository;
import by.itacademy.classifier.entity.Category;
import by.itacademy.classifier.service.api.ICategoryService;
import by.itacademy.classifier.dto.util.mapper.CategoryToDtoMapper;
import by.itacademy.classifier.dto.util.validator.CategoryDtoValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        return mapper.entityToDto(savedCategory);
    }

    @Override
    public CategoryPageDto getCategoryPage(int page, int size) {
        categoryDtoValidator.validatePage(page, size);
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        List<Category> categories = categoryRepository.findAll(pageable).getContent();
        int totalElements = categoryRepository.findAll().size();
        int totalPages = totalElements / size;
        List<CategoryDto> dtoList = categories.stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return CategoryPageDto.builder()
                .number(page)
                .size(size)
                .totalPages(totalPages == 0 ? 1 : totalPages)
                .totalElements(totalElements)
                .first(page == 1)
                .numberOfElements(categories.size())
                .last(isLastElement(pageable, totalElements))
                .content(dtoList)
                .build();
    }
}
