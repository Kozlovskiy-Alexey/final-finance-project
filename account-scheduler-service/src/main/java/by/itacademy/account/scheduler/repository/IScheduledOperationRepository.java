package by.itacademy.account.scheduler.repository;

import by.itacademy.account.scheduler.repository.entity.Operation;
import by.itacademy.account.scheduler.repository.entity.Schedule;
import by.itacademy.account.scheduler.repository.entity.ScheduledOperation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IScheduledOperationRepository extends PagingAndSortingRepository<ScheduledOperation, String> {

    @Query("select s from ScheduledOperation s where s.id like :partId")
    Optional<ScheduledOperation> findByIdContaining(@Param("partId") String partId);

    @Query("select sch from ScheduledOperation sch where sch.scheduleId = ?1 and sch.operationId = ?2")
    List<ScheduledOperation> findAllByScheduleAndOperationId(Schedule sch, Operation operation);
}
