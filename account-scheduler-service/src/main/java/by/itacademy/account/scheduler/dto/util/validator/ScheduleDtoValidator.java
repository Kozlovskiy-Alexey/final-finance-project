package by.itacademy.account.scheduler.dto.util.validator;

import by.itacademy.account.scheduler.advice.ResponseError;
import by.itacademy.account.scheduler.advice.ValidateException;
import by.itacademy.account.scheduler.dto.ScheduleDto;
import by.itacademy.account.scheduler.dto.api.TimeUnit;
import by.itacademy.account.scheduler.dto.util.validator.api.IDtoValidator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class ScheduleDtoValidator implements IDtoValidator<ScheduleDto> {

    @Override
    public void validateDto(ScheduleDto dto) {
        validateStartStopTime(dto.getStartTime(), dto.getStopTime(), dto.getInterval());
        try {
            TimeUnit.valueOf(dto.getTimeUnit());
        } catch (IllegalArgumentException ex) {
            throw new ValidateException(List.of(new ResponseError("time unit is not correct")));
        }
    }

    private void validateStartStopTime(LocalDateTime startTime, LocalDateTime stopTime, int interval) {
        long start = startTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        long stop = stopTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        validateLongDateTimeFormat(start);
        validateLongDateTimeFormat(stop);
        if (interval < 0) {
            throw new ValidateException(List.of(new ResponseError("interval should be positive")));
        }
        if (start > stop) {
            throw new ValidateException(List.of(new ResponseError("start time shouldn't be more then stop time")));
        }
    }
}
