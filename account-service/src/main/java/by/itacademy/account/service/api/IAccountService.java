package by.itacademy.account.service.api;


import by.itacademy.account.dto.AccountDto;
import by.itacademy.account.dto.AccountPageDto;

public interface IAccountService extends IBaseAccountService{

    /**
     * Добавление нового счёта
     * @param account счет
     */
    AccountDto add(AccountDto account);

    /**
     * Получение страницы счетов
     * @param pageNumber номер страницы
     * @param size размер страницы
     * @return страницу счетов
     */
    AccountPageDto getPage(int pageNumber, int size);

    /**
     * Получение информации по счету
     * @param uuid универсальный уникальный идентификатор счета
     * @return счет
     */
    AccountDto get(String uuid);

    /**
     * Редактирование информации о счете
     * @param uuid универсальный уникальный идентификатор счета
     * @param lastUpdate дата последнего обновления записи
     * @param accountDTO данные для обновления
     */
    AccountDto update(String uuid, long lastUpdate, AccountDto accountDTO);
}
