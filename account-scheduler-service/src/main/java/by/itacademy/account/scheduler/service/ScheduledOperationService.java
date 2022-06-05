package by.itacademy.account.scheduler.service;

import by.itacademy.account.scheduler.advice.ResponseError;
import by.itacademy.account.scheduler.advice.SingleValidateException;
import by.itacademy.account.scheduler.advice.ValidateException;
import by.itacademy.account.scheduler.dto.OperationDto;
import by.itacademy.account.scheduler.dto.ScheduleDto;
import by.itacademy.account.scheduler.dto.ScheduledOperationDto;
import by.itacademy.account.scheduler.dto.ScheduledOperationPageDto;
import by.itacademy.account.scheduler.dto.util.mapper.ScheduledOperationToDtoMapper;
import by.itacademy.account.scheduler.dto.util.validator.OperationDtoValidator;
import by.itacademy.account.scheduler.dto.util.validator.ScheduleDtoValidator;
import by.itacademy.account.scheduler.repository.IOperationRepository;
import by.itacademy.account.scheduler.repository.IScheduleRepository;
import by.itacademy.account.scheduler.repository.IScheduledOperationRepository;
import by.itacademy.account.scheduler.repository.entity.ScheduledOperation;
import by.itacademy.account.scheduler.service.api.IScheduledOperationService;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduledOperationService implements IScheduledOperationService {

    private final OperationDtoValidator operationDtoValidator;
    private final ScheduleDtoValidator scheduleDtoValidator;
    private final IScheduledOperationRepository scheduledOperationRepository;
    private final ScheduledOperationToDtoMapper scheduledOperationToDtoMapper;
    private final SchedulerService schedulerService;
    private final IOperationRepository operationRepository;
    private final IScheduleRepository scheduleRepository;

    public ScheduledOperationService(OperationDtoValidator operationDtoValidator,
                                     ScheduleDtoValidator scheduleDtoValidator,
                                     IScheduledOperationRepository scheduledOperationRepository,
                                     ScheduledOperationToDtoMapper scheduledOperationToDtoMapper,
                                     SchedulerService schedulerService, IOperationRepository operationRepository,
                                     IScheduleRepository scheduleRepository) {
        this.operationDtoValidator = operationDtoValidator;
        this.scheduleDtoValidator = scheduleDtoValidator;
        this.scheduledOperationRepository = scheduledOperationRepository;
        this.scheduledOperationToDtoMapper = scheduledOperationToDtoMapper;
        this.schedulerService = schedulerService;
        this.operationRepository = operationRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public ScheduledOperationDto add(ScheduledOperationDto dto) {
        ScheduleDto scheduleDto = dto.getScheduleDto();
        OperationDto operationDto = dto.getOperationDto();
        scheduleDtoValidator.validateDto(scheduleDto);
        operationDtoValidator.validateDto(operationDto);

        ScheduledOperation scheduledOperation = scheduledOperationToDtoMapper.dtoToEntity(dto);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        scheduledOperation.setUserLogin(userDetails.getUsername());

        operationRepository.save(scheduledOperation.getOperationId());

        scheduleRepository.save(scheduledOperation.getScheduleId());
        ScheduledOperation entity = scheduledOperationRepository.save(scheduledOperation);
        schedulerService.create(entity.getScheduleId(), entity.getId());

        return scheduledOperationToDtoMapper.entityToDto(entity);
    }

    @Override
    public ScheduledOperationPageDto get(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        List<ScheduledOperation> scheduledOperations = scheduledOperationRepository.findAll(pageable).getContent();
        int totalElements = scheduledOperationRepository.findAll().size();
        int totalPages = totalElements / size;
        List<ScheduledOperationDto> scheduledOperationsDto = scheduledOperations.stream()
                .map(scheduledOperationToDtoMapper::entityToDto)
                .collect(Collectors.toList());
        return ScheduledOperationPageDto.builder()
                .number(page)
                .size(size)
                .totalPages(totalPages == 0 ? 1 : totalPages)
                .totalElements(totalElements)
                .first(page == 1)
                .numberOfElements(scheduledOperations.size())
                .last(isLastElement(pageable, totalElements))
                .content(scheduledOperationsDto)
                .build();
    }

    @Override
    public ScheduledOperationDto update(String operationUuid, long lastUpdate, ScheduledOperationDto dto) {
        operationDtoValidator.validateUuid(operationUuid);
        operationDtoValidator.validateLongDateTimeFormat(lastUpdate);
        ScheduledOperation operation = null;
        try {
            operation = scheduledOperationRepository.getById(operationUuid);
        } catch (EntityNotFoundException ex) {
            throw new ValidateException(List.of(new ResponseError("there is no operation id " + operationUuid +
                    " in database")));
        }
        LocalDateTime dateTime = Instant.ofEpochMilli(lastUpdate).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime dtUpdate = operation.getDtUpdate();
        if (dtUpdate.equals(dateTime)) {
            boolean isDelete = deleteJobAndTriggers(operationUuid);
            ScheduledOperation scheduledOperation = scheduledOperationToDtoMapper.dtoToEntity(dto);
            scheduledOperation.setId(operationUuid);
            scheduledOperation.setDtUpdate(LocalDateTime.now());
            operationRepository.save(scheduledOperation.getOperationId());
            scheduleRepository.save(scheduledOperation.getScheduleId());

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            scheduledOperation.setUserLogin(userDetails.getUsername());
            ScheduledOperation save = scheduledOperationRepository.save(scheduledOperation);
            return scheduledOperationToDtoMapper.entityToDto(save);
        } else {
            throw new ValidateException(List.of(new ResponseError("dt_update " + lastUpdate + " does not match in database")));
        }
    }

    private boolean deleteJobAndTriggers(String jobKey) {
        try {
            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler scheduler = sf.getScheduler();
            return scheduler.deleteJob(JobKey.jobKey(jobKey));
        } catch (SchedulerException e) {
            throw new SingleValidateException(new ResponseError("Error while updating scheduler."));
        }
    }
}