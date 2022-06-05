package by.itacademy.account.scheduler.service.api;


import by.itacademy.account.scheduler.dto.AccountDto;
import by.itacademy.account.scheduler.dto.CategoryDto;
import by.itacademy.account.scheduler.dto.CurrencyDto;
import by.itacademy.account.scheduler.dto.OperationDto;

public interface IRestService {

    CurrencyDto getCurrency(String currencyId);

    CategoryDto getCategory(String typeId);

    AccountDto getAccount(String accountId);

    void putOperation(OperationDto operationDto, String userLogin);
}