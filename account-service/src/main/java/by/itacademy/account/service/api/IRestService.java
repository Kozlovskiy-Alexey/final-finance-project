package by.itacademy.account.service.api;

import by.itacademy.account.dto.CategoryDto;
import by.itacademy.account.dto.CurrencyDto;

public interface IRestService {

    CurrencyDto getCurrency(String currencyId);

    CategoryDto getCategory(String typeId);
}
