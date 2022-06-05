package by.itacademy.classifier.service.api;

import org.springframework.data.domain.Pageable;

public interface IBaseClassifierService {

    default boolean isLastElement(Pageable pageable, int totalElements) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        return (pageSize * pageNumber < totalElements);
    }
}
