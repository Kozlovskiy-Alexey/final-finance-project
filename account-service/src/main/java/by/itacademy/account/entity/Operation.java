package by.itacademy.account.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(schema = "finance_app", name = "operation")
public class Operation implements Serializable {

    @Id
    @Column(name = "id")
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account accountUuid;

    @Column(name = "dt_create")
    private LocalDateTime dtCreate;

    @Column(name = "dt_update")
    private LocalDateTime dtUpdate;

    @Column(name = "dt_execute")
    private LocalDateTime dtExecute;

    @Column(name = "description")
    private String description;

    @Column(name = "operation_category_id")
    private String operationCategory;

    @Column(name = "operation_value")
    private double operationValue;

    @Column(name = "currency_id")
    private String currencyUuid;

}
