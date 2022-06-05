package by.itacademy.account.scheduler.repository;

import by.itacademy.account.scheduler.IntegrationTestBase;
import by.itacademy.account.scheduler.repository.entity.Operation;
import by.itacademy.account.scheduler.repository.entity.Schedule;
import by.itacademy.account.scheduler.repository.entity.ScheduledOperation;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

class IScheduledOperationRepositoryTest extends IntegrationTestBase {

    @Autowired
    private IScheduledOperationRepository scheduledOperationRepository;

    @Test
    void testFindById() {
        Optional<ScheduledOperation> id = scheduledOperationRepository.findById(SCHEDULED_OPERATION_ID);
        assertTrue(id.isPresent());
    }

    @Test
    void testFindByPartId() {
        Optional<ScheduledOperation> scheduledOperation = scheduledOperationRepository
                .findByIdContaining("6e52e4cfb883");
        assertTrue(scheduledOperation.isPresent());
    }

    @Test
    void testFindByOperationIdAndScheduleId() {
        Schedule schedule = scheduleRepository.getById(SCHEDULE_ID);
        Operation operation = operationRepository.getById(OPERATION_ID);
        List<ScheduledOperation> scheduledOperations = scheduledOperationRepository
                .findAllByScheduleAndOperationId(schedule, operation);
        assertFalse(scheduledOperations.isEmpty());
        assertThat(scheduledOperations, IsCollectionWithSize.hasSize(1));
    }
}