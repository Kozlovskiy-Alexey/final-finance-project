package by.itacademy.classifier.service.api;

import by.itacademy.classifier.dto.CategoryDto;
import by.itacademy.classifier.dto.CategoryPageDto;

public interface ICategoryService extends IBaseClassifierService {

    CategoryDto add(CategoryDto categoryDto);

    CategoryPageDto getCategoryPage(int page, int size);
}
