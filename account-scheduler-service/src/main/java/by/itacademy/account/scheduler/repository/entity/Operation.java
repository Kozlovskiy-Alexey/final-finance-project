package by.itacademy.account.scheduler.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(schema = "scheduler", name = "operation")
public class Operation implements Serializable {

    @Id
    private String id;

    @Column(name = "account_id")
    private String accountId;

    private String description;

    @Column(name = "op_value")
    private double value;

    @Column(name = "currency_id")
    private String currencyID;

    @Column(name = "op_category")
    private String category;

}

