package by.itacademy.report.entity;

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
@Table(schema = "report", name = "report_info")
public class ReportInfo implements Serializable {
    @Id
    private String id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "report_id")
    private String reportId;

}
