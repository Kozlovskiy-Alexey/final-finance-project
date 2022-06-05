package by.itacademy.report.service.api;

import by.itacademy.report.dto.ReportDto;
import by.itacademy.report.dto.ReportPageDto;
import by.itacademy.report.dto.RequestParamsDto;
import by.itacademy.report.entity.Report;
import by.itacademy.report.service.ReportType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@Service
public interface IReportService extends IBaseReportService {

    ReportDto create(ReportType reportType, RequestParamsDto params);

    ReportPageDto getReportPage(int page, int size);

    ByteArrayInputStream getReport(String reportId);

    Optional<Report> checkReport(String reportId);
}
