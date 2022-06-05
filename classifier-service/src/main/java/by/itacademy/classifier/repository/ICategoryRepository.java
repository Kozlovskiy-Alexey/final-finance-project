package by.itacademy.classifier.repository;

import by.itacademy.classifier.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, String> {

}
