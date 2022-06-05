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
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "finance_app", name = "balance")
public class Balance implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    private double balance;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

}
