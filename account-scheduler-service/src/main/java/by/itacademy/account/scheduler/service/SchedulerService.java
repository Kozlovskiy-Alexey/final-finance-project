package by.itacademy.account.scheduler.service;

import by.itacademy.account.scheduler.advice.ResponseError;
import by.itacademy.account.scheduler.advice.ValidateException;
import by.itacademy.account.scheduler.dto.api.TimeUnit;
import by.itacademy.account.scheduler.repository.entity.Schedule;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class SchedulerService {

    private final Scheduler scheduler;

    public SchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void create(Schedule schedule, String scheduledOperationId) {

        JobDetail job = JobBuilder.newJob(CreateOperationJob.class)
                .withIdentity(scheduledOperationId, "operations")
                .usingJobData("operation", scheduledOperationId)
                .build();

        int interval = (int) schedule.getInterval();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(scheduledOperationId, "operations")
                .startAt(Date.from(schedule.getStartTime().atZone(ZoneId.systemDefault()).toInstant()))
                .withSchedule(TimeUnit.valueOf(schedule.getTimeUnit()).getScheduleBuilder(interval))
                .build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException ex) {
            throw new ValidateException(List.of(new ResponseError("ошибка quartz")));
        }
    }

}
