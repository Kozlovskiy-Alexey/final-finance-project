package by.itacademy.account.scheduler.service;

import by.itacademy.account.scheduler.dto.OperationDto;
import by.itacademy.account.scheduler.dto.util.mapper.OperationToDtoMapper;
import by.itacademy.account.scheduler.repository.IScheduledOperationRepository;
import by.itacademy.account.scheduler.repository.entity.Operation;
import by.itacademy.account.scheduler.repository.entity.ScheduledOperation;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class CreateOperationJob implements Job {

    private final IScheduledOperationRepository scheduledOperationRepository;
    private final RestSchedulerService restSchedulerService;
    private final OperationToDtoMapper operationToDtoMapper;

    public CreateOperationJob(IScheduledOperationRepository scheduledOperationRepository,
                              RestSchedulerService restSchedulerService, OperationToDtoMapper operationToDtoMapper) {
        this.scheduledOperationRepository = scheduledOperationRepository;
        this.restSchedulerService = restSchedulerService;
        this.operationToDtoMapper = operationToDtoMapper;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String operationId = jobExecutionContext.getMergedJobDataMap().getString("operation");
        ScheduledOperation scheduledOperation = scheduledOperationRepository.findById(operationId).get();
        String userLogin = scheduledOperation.getUserLogin();
        Operation operation = scheduledOperation.getOperationId();
        OperationDto operationDto = operationToDtoMapper.entityToDto(operation);
        restSchedulerService.putOperation(operationDto, userLogin);
    }
}
