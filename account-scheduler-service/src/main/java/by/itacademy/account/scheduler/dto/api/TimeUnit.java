package by.itacademy.account.scheduler.dto.api;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;

public enum TimeUnit implements ICustomSchedulerBuilder  {

    SECOND {
        @Override
        public SimpleScheduleBuilder getScheduleBuilder(int interval) {
            return SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(interval).repeatForever();
        }
    },
    MINUTE {
        @Override
        public SimpleScheduleBuilder getScheduleBuilder(int interval) {
            return SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(interval).repeatForever();
        }
    },
    HOUR {
        @Override
        public SimpleScheduleBuilder getScheduleBuilder(int interval) {
            return SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(interval).repeatForever();
        }
    },
    DAY {
        @Override
        public SimpleScheduleBuilder getScheduleBuilder(int interval) {
            return SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(interval * 24).repeatForever();
        }
    },
    WEEK {
        @Override
        public SimpleScheduleBuilder getScheduleBuilder(int interval) {
            return SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(interval * 24 * 7).repeatForever();
        }
    },
    MONTH {
        @Override
        public CalendarIntervalScheduleBuilder getScheduleBuilder(int interval) {
            return CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMonths(interval);
        }
    },
    YEAR {
        @Override
        public CalendarIntervalScheduleBuilder getScheduleBuilder(int interval) {
            return CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInYears(interval);
        }
    }
}
