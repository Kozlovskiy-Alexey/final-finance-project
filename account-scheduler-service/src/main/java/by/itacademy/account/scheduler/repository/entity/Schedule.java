package by.itacademy.account.scheduler.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(schema = "scheduler", name = "schedule")
public class Schedule {

    @Id
    private String id;
    @Column(name = "start_time")

    private LocalDateTime startTime;
    @Column(name = "stop_time")

    private LocalDateTime stopTime;

    @Column(name = "sch_interval")
    private int interval;

    @Column(name = "time_unit")
    private String timeUnit;

}
