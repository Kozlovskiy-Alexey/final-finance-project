package by.itacademy.account.scheduler.service.api;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface IBaseSchedulerService {

    default long getMilliSecondFromLocalDateTime(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
