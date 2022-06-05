package by.itacademy.report.repository;

import by.itacademy.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReportRepository extends JpaRepository<Report, String > {
}
