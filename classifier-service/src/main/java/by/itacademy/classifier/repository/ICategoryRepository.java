package by.itacademy.classifier.repository;

import by.itacademy.classifier.entity.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ICategoryRepository extends PagingAndSortingRepository<Category, String> {

}
