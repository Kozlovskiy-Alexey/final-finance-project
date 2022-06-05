package by.itacademy.report.service.api;

import by.itacademy.report.dto.AccountDto;
import by.itacademy.report.dto.AccountPageDto;
import by.itacademy.report.dto.CategoryDto;
import by.itacademy.report.dto.CategoryPageDto;
import by.itacademy.report.dto.CurrencyDto;
import by.itacademy.report.dto.CurrencyPageDto;
import by.itacademy.report.dto.OperationPageDto;

public interface IRestService {

    CurrencyDto getCurrency(String currencyId);

    CurrencyPageDto getCurrencyPage();

    CategoryDto getCategory(String typeId);

    CategoryPageDto getCategoryPage();

    OperationPageDto getOperationPage(String accountId);

    AccountPageDto getAccountPage();

    AccountDto getAccount(String accountId);

}
