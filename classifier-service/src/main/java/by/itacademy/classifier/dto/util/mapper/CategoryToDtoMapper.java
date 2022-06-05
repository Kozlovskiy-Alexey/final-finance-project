package by.itacademy.classifier.dto.util.mapper;

import by.itacademy.classifier.dto.CategoryDto;
import by.itacademy.classifier.dto.util.mapper.api.IRepositoryMapper;
import by.itacademy.classifier.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryToDtoMapper implements IRepositoryMapper<Category, CategoryDto> {

    @Override
    public CategoryDto entityToDto(Category entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .dtCreate(entity.getDtCreate())
                .dtUpdate(entity.getDtUpdate())
                .title(entity.getTitle())
                .build();
    }

    @Override
    public Category dtoToEntity(CategoryDto dto) {
        return Category.builder()
                .title(dto.getTitle())
                .build();
    }
}
