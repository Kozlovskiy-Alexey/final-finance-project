package by.itacademy.account.scheduler.service.api;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface IBaseSchedulerService {

    default boolean isLastElement(Pageable pageable, int totalElements) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        return (pageSize * pageNumber < totalElements);
    }

    default long getMilliSecondFromLocalDateTime(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
