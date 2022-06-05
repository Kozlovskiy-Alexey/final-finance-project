package by.itacademy.account.scheduler.dto.util.mapper;

import by.itacademy.account.scheduler.dto.OperationDto;
import by.itacademy.account.scheduler.dto.ScheduleDto;
import by.itacademy.account.scheduler.dto.ScheduledOperationDto;
import by.itacademy.account.scheduler.dto.util.mapper.api.IRepositoryMapper;
import by.itacademy.account.scheduler.repository.entity.Operation;
import by.itacademy.account.scheduler.repository.entity.Schedule;
import by.itacademy.account.scheduler.repository.entity.ScheduledOperation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ScheduledOperationToDtoMapper implements IRepositoryMapper<ScheduledOperation, ScheduledOperationDto> {

    @Override
    public ScheduledOperationDto entityToDto(ScheduledOperation entity) {

        Schedule schedule = entity.getScheduleId();
        Operation operation = entity.getOperationId();

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .startTime(schedule.getStartTime())
                .stopTime(schedule.getStopTime())
                .interval(schedule.getInterval())
                .timeUnit(schedule.getTimeUnit())
                .build();

        OperationDto operationDto = OperationDto.builder()
                .accountId(operation.getAccountId())
                .description(operation.getDescription())
                .value(operation.getValue())
                .currencyId(operation.getCurrencyID())
                .categoryId(operation.getCategory())
                .build();

        return ScheduledOperationDto.builder()
                .id(entity.getId())
                .dtCreate(entity.getDtCreate())
                .dtUpdate(entity.getDtUpdate())
                .scheduleDto(scheduleDto)
                .operationDto(operationDto)
                .build();
    }

    @Override
    public ScheduledOperation dtoToEntity(ScheduledOperationDto dto) {

        ScheduleDto scheduleDto = dto.getScheduleDto();
        OperationDto operationDto = dto.getOperationDto();

        Schedule schedule = Schedule.builder()
                .id(UUID.randomUUID().toString())
                .startTime(scheduleDto.getStartTime())
                .stopTime(scheduleDto.getStopTime())
                .interval(scheduleDto.getInterval())
                .timeUnit(scheduleDto.getTimeUnit())
                .build();

        Operation operation = Operation.builder()
                .id(UUID.randomUUID().toString())
                .accountId(operationDto.getAccountId())
                .value(operationDto.getValue())
                .description(operationDto.getDescription())
                .currencyID(operationDto.getCurrencyId())
                .category(operationDto.getCategoryId())
                .build();

        return ScheduledOperation.builder()
                .id(UUID.randomUUID().toString())
                .operationId(operation)
                .dtCreate(LocalDateTime.now())
                .dtUpdate(LocalDateTime.now())
                .scheduleId(schedule)
                .build();
    }
}
