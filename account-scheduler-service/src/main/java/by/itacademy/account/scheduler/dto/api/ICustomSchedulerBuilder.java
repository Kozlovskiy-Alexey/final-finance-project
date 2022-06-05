package by.itacademy.account.scheduler.dto.api;

import org.quartz.ScheduleBuilder;

public interface ICustomSchedulerBuilder {

    ScheduleBuilder<?> getScheduleBuilder(int interval);
}
