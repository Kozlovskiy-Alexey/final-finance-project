package by.itacademy.account.entity;

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
@Builder
@Data
@Entity
@Table(schema = "finance_app", name = "account_type")
public class AccountType implements Serializable {
    @Id
    @Column(name = "id")
    private String accountTypeId;

    private String description;
}
