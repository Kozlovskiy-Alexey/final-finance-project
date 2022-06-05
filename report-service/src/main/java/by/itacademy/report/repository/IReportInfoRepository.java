package by.itacademy.report.repository;

import by.itacademy.report.entity.ReportInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IReportInfoRepository extends JpaRepository<ReportInfo, String> {

    @Query("select r from ReportInfo r where r.reportId = ?1")
    List<ReportInfo> findAllByAccountIdEqualsAndAccountId(String reportId);

}
