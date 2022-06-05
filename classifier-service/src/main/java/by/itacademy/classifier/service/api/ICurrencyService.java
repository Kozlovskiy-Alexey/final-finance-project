package by.itacademy.classifier.service.api;

import by.itacademy.classifier.dto.CurrencyDto;
import by.itacademy.classifier.dto.CurrencyPageDto;

public interface ICurrencyService extends IBaseClassifierService {

    /**
     * Добавление новой валюты
     * @param currencyDTO тело для валюты
     */
    CurrencyDto addCurrency(CurrencyDto currencyDTO);

    /**
     * Получить страницу валют
     * @return CurrencyPageDTO страница валют
     */
    CurrencyPageDto getCurrencyPage(int page, int size);
}
