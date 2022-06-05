package by.itacademy.account.scheduler.service.api;

import by.itacademy.account.scheduler.dto.ScheduledOperationDto;
import by.itacademy.account.scheduler.dto.ScheduledOperationPageDto;

public interface IScheduledOperationService extends IBaseSchedulerService {

    ScheduledOperationDto add(ScheduledOperationDto dto);

    ScheduledOperationPageDto get(int page, int size);

    ScheduledOperationDto update(String OperationUuid, long lastUpdate, ScheduledOperationDto dto);
}
