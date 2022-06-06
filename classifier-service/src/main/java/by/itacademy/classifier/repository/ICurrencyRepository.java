package by.itacademy.classifier.repository;

import by.itacademy.classifier.entity.Currency;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ICurrencyRepository extends PagingAndSortingRepository<Currency, String> {

}
