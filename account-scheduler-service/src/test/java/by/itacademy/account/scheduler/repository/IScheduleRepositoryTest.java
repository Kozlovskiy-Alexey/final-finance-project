package by.itacademy.account.scheduler.repository;

import by.itacademy.account.scheduler.IntegrationTestBase;
import by.itacademy.account.scheduler.dto.api.TimeUnit;
import by.itacademy.account.scheduler.repository.entity.Operation;
import by.itacademy.account.scheduler.repository.entity.Schedule;
import by.itacademy.account.scheduler.repository.entity.ScheduledOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IScheduleRepositoryTest extends IntegrationTestBase {

    @Value("${app.services.schedule.message}")
    private String valueExample;

    @Test
    void testGetById() {
        ScheduledOperation byId = scheduledOperationRepository.getById(SCHEDULED_OPERATION_ID);
        assertNotNull(byId.getId());
        assertEquals(SCHEDULED_OPERATION_ID, byId.getId());
    }

    @Test
    void testSaveScheduleToDataBase() {
        Schedule schedule = Schedule.builder()
                .id(SCHEDULE_ID)
                .startTime(LocalDateTime.now().plusSeconds(15))
                .stopTime(LocalDateTime.now().plusSeconds(360))
                .interval(1)
                .timeUnit(TimeUnit.DAY.name())
                .build();
        Schedule save = scheduleRepository.save(schedule);
        assertNotNull(save);
    }

    @Test
    void testSaveOperationToDataBase() {
        Operation operation = Operation.builder()
                .id(OPERATION_ID)
                .accountId(UUID.randomUUID().toString())
                .description("description")
                .currencyID(UUID.randomUUID().toString())
                .category(UUID.randomUUID().toString())
                .build();
        Operation save = operationRepository.save(operation);
        assertNotNull(save);
    }

    @Test
    void testSaveScheduledOperationWithOperationAndSchedule() {
        Schedule schedule = Schedule.builder()
                .id(SCHEDULE_ID)
                .startTime(LocalDateTime.now().plusSeconds(15))
                .stopTime(LocalDateTime.now().plusSeconds(360))
                .interval(1)
                .timeUnit(TimeUnit.DAY.name())
                .build();
        Operation operation = Operation.builder()
                .id(OPERATION_ID)
                .accountId(UUID.randomUUID().toString())
                .description("description")
                .currencyID(UUID.randomUUID().toString())
                .category(UUID.randomUUID().toString())
                .build();

        ScheduledOperation scheduledOperation = ScheduledOperation.builder()
                .id(UUID.randomUUID().toString())
                .operationId(operation)
                .dtCreate(LocalDateTime.now())
                .dtUpdate(LocalDateTime.now())
                .scheduleId(schedule)
                .build();

        ScheduledOperation save = scheduledOperationRepository.save(scheduledOperation);
        assertNotNull(save);
    }
}