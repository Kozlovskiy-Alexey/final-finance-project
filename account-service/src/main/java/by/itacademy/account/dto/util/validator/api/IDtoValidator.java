package by.itacademy.account.dto.util.validator.api;

import by.itacademy.account.advice.ResponseError;
import by.itacademy.account.advice.ValidateException;
import by.itacademy.account.dto.api.IDto;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface IDtoValidator<D extends IDto> {
    String REGEX = "[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}";

    void validateDto(D iDto);

    default boolean validateUuid(String uuid) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}");
        Matcher matcher = pattern.matcher(uuid);
        return matcher.find();
    }

    default void validateLongDateTimeFormat(long dateTime) {
        Pattern pattern = Pattern.compile("\\d{13}");
        Matcher matcher = pattern.matcher("" + dateTime);
        if (!matcher.find()) {
            throw new ValidateException(List.of(new ResponseError("date is not correct")));
        }
    }

    default void validatePage(int pageNumber, int size) {
        if (pageNumber < 0 || size < 0) {
            throw new ValidateException(List.of(new ResponseError("page or size should be more then 0 and positive")));
        }
    }

}
