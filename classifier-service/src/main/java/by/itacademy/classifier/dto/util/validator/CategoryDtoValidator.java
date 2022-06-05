package by.itacademy.classifier.dto.util.validator;

import by.itacademy.classifier.advice.ValidateException;
import by.itacademy.classifier.dto.CategoryDto;
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
        if (validateCurrency(dto.getTitle())) {
            validateException.addError("category " + dto.getTitle() + " is already in the database");
        }
        if (!validateException.getResponseErrors().isEmpty()) {
            throw new ValidateException(validateException.getResponseErrors());
        }
    }

    private boolean validateCurrency(String category) {
        return categoryRepository.findAll().stream()
                .anyMatch(o -> o.getTitle().equals(category));
    }
}
