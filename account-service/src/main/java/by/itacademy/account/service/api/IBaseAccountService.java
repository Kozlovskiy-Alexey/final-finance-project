package by.itacademy.account.service.api;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface IBaseAccountService {

    default long getMilliSecondFromLocalDateTime(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
