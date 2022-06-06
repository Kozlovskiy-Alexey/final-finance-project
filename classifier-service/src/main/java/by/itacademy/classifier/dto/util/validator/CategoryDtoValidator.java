package by.itacademy.classifier.dto.util.validator;

import by.itacademy.classifier.advice.ValidateException;
import by.itacademy.classifier.dto.CategoryDto;
import by.itacademy.classifier.entity.Category;
import by.itacademy.classifier.repository.ICategoryRepository;
import by.itacademy.classifier.dto.util.validator.api.IDtoValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CategoryDtoValidator implements IDtoValidator<CategoryDto> {

    private final ICategoryRepository categoryRepository;

    public CategoryDtoValidator(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void validateDto(CategoryDto dto) {
        ValidateException validateException = new ValidateException(new ArrayList<>());
        if (dto.getTitle().isEmpty()) {
            validateException.addError("category shouldn't be empty");
        }
        if (validateCategory(dto.getTitle())) {
            validateException.addError("category " + dto.getTitle() + " is already in the database");
        }
        if (!validateException.getResponseErrors().isEmpty()) {
            throw new ValidateException(validateException.getResponseErrors());
        }
    }

    private boolean validateCategory(String category) {
        for (Category c : categoryRepository.findAll()) {
            if (!c.getTitle().equals(category)) {
                return false;
            }
        }
        return true;
    }
}
