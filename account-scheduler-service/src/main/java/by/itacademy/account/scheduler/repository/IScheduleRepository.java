package by.itacademy.account.scheduler.repository;

import by.itacademy.account.scheduler.repository.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IScheduleRepository extends JpaRepository<Schedule, String> {
}
