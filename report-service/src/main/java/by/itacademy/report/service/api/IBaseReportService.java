package by.itacademy.report.service.api;

import by.itacademy.report.dto.RequestParamsDto;
import by.itacademy.report.service.ReportType;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public interface IBaseReportService {

    default boolean isLastElement(Pageable pageable, int totalElements) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        return (pageSize * pageNumber < totalElements);
    }

    default long getMilliSecondFromLocalDateTime(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    default String getDescription(ReportType type, RequestParamsDto params) {
        String from = params.getFrom().toString();
        String to = params.getTo().toString();
        List<String> accounts = params.getAccounts();
        List<String> categories = params.getCategories();
        StringBuilder sb = new StringBuilder();
        String description = String.format("отчет по %s за период c %s по %s, accounts: ", type.toString(),
                from.toString(), to.toString());
        sb.append(description);

        for (String account : accounts) {
            sb.append(account);
            sb.append(",");
        }
        sb.append("categories:");
        for (String category : categories) {
            sb.append(category);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1).append(".");
        return sb.toString();
    }

    default String getDescriptionByBalanceReport(ReportType type, RequestParamsDto params) {
        List<String> accounts = params.getAccounts();
        String description = String.format("отчет за весь период по %s, accounts: ", type.toString());
        StringBuilder sb = new StringBuilder();
        sb.append(description);

        for (String account : accounts) {
            sb.append(account);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1).append(".");
        return sb.toString();
    }
}
