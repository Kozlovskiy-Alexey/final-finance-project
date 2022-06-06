package by.itacademy.report.repository;

import by.itacademy.report.entity.Report;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IReportRepository extends PagingAndSortingRepository<Report, String > {
}
