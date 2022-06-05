package by.itacademy.account.scheduler;

import by.itacademy.account.scheduler.repository.IOperationRepository;
import by.itacademy.account.scheduler.repository.IScheduleRepository;
import by.itacademy.account.scheduler.repository.IScheduledOperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTestBase {

    public final static String SCHEDULE_ID = "2477ce9a-0bbf-4313-8e23-dfb06c463f36";
    public final static String OPERATION_ID = "095d3de0-c809-42f1-8164-061f17b1b746";
    public final static String SCHEDULED_OPERATION_ID = "f188e477-42ec-4db1-a04d-6e52e4cfb883";

    @Autowired
    public IScheduleRepository scheduleRepository;
    @Autowired
    public IScheduledOperationRepository scheduledOperationRepository;
    @Autowired
    public IOperationRepository operationRepository;
}
