package by.itacademy.account.service.api;

import by.itacademy.account.dto.OperationDto;
import by.itacademy.account.dto.OperationPageDto;

public interface IOperationService extends IBaseAccountService {

    /**
     * Добавление операции по счету
     * @param accountUuid универсальный уникальный идентификатор счета
     * @param operation операция по счету
     */
    OperationDto add(String accountUuid, OperationDto operation);

    /**
     * Получение страницы операций по счету
     * @param uuid универсальный уникальный идентификатор счета
     */
    OperationPageDto get(String uuid, int pageNumber, int size);

    /**
     * Редактирование информации об операции на счете
     * @param accountUuid универсальный уникальный идентификатор счета
     * @param operationUuid универсальный уникальный идентификатор операции
     * @param lastUpdate дата последнего обновления записи операции
     * @param operationDTO операция по счету
     */
    OperationDto update(String accountUuid, String operationUuid, long lastUpdate, OperationDto operationDTO);

    /**
     * Удаление операции на счете
     * @param uuidAccount идентификатор счёта в котором редактируем операцию
     * @param uuidOperation Идентификатор операции в котором редактируем
     * @param dtUpdate дата последнего обновления записи
     */
    void delete(String uuidAccount, String uuidOperation, long dtUpdate);
}
