package by.itacademy.classifier.repository;

import by.itacademy.classifier.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICurrencyRepository extends JpaRepository<Currency, String> {

}
