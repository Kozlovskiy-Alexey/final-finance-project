package by.itacademy.account.scheduler.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(schema = "scheduler", name = "scheduled_operation")
public class ScheduledOperation {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "operation_id")
    private Operation operationId;

    @Column(name = "dt_create")
    private LocalDateTime dtCreate;

    @Column(name = "dt_update")
    private LocalDateTime dtUpdate;

    @OneToOne
    @JoinColumn(name = "schedule_id")
    private Schedule scheduleId;

    @Column(name = "user_login")
    private String userLogin;

}
