package by.itacademy.account.scheduler.dto.util.validator.api;


import by.itacademy.account.scheduler.advice.ResponseError;
import by.itacademy.account.scheduler.advice.SingleValidateException;
import by.itacademy.account.scheduler.dto.api.IDto;
import org.springframework.data.domain.Pageable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface IDtoValidator<D extends IDto> {
    String REGEX = "[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}";

    void validateDto(D iDto);

    default boolean validateUuid(String uuid) {
        Pattern pattern = Pattern.compile("[\\d\\w]{8}-[\\d\\w]{4}-[\\d\\w]{4}-[\\d\\w]{4}-[\\d\\w]{12}");
        Matcher matcher = pattern.matcher(uuid);
        return matcher.find();
    }

    default Pageable validatePage(int pageNumber, int size) {
        if (pageNumber < 0 || size < 0) {
            throw new SingleValidateException(new ResponseError("page or size should be more then 0 and positive"));
        }
        return Pageable.ofSize(size).withPage(pageNumber);
    }

    default void validateValue(double value) {
        Pattern pattern = Pattern.compile("-?\\d+\\.?\\d*");
        Matcher matcher = pattern.matcher("" + value);
        boolean isFindDigit = matcher.find();
        if (!isFindDigit) {
            throw new SingleValidateException(new ResponseError("value is not correct"));
        }
    }

    default void validateLongDateTimeFormat(long dateTime) {
        Pattern pattern = Pattern.compile("\\d{13}");
        Matcher matcher = pattern.matcher("" + dateTime);
        if (!matcher.find()) {
            throw new SingleValidateException(new ResponseError("time is not correct"));
        }
    }
}
