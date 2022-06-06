package by.itacademy.classifier.dto.util.validator;

import by.itacademy.classifier.advice.MultipleValidateException;
import by.itacademy.classifier.dto.CategoryDto;
import by.itacademy.classifier.entity.Category;
import by.itacademy.classifier.repository.ICategoryRepository;
import by.itacademy.classifier.dto.util.validator.api.IDtoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
public class CategoryDtoValidator implements IDtoValidator<CategoryDto> {

    private final ICategoryRepository categoryRepository;

    public CategoryDtoValidator(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void validateDto(CategoryDto dto) {
        MultipleValidateException multipleValidateException = new MultipleValidateException(new ArrayList<>());
        if (dto.getTitle().isEmpty()) {
            multipleValidateException.addError("Category shouldn't be empty.");
        }
        if (!validateCategory(dto.getTitle())) {
            multipleValidateException.addError("Category " + dto.getTitle() + " is already in the database.");
        }
        if (!multipleValidateException.getResponseErrors().isEmpty()) {
            log.error("CategoryDto is not valid.", multipleValidateException);
            throw new MultipleValidateException(multipleValidateException.getResponseErrors());
        }
    }

    private boolean validateCategory(String category) {
        for (Category c : categoryRepository.findAll()) {
            if (c.getTitle().equals(category)) {
                log.error("Category " + category + " is not valid.");
                return false;
            }
        }
        return true;
    }
}
