package by.itacademy.report.service.handler;

import by.itacademy.report.service.ReportType;
import by.itacademy.report.service.RestReportService;
import by.itacademy.report.service.handler.api.IReportHandler;
import org.springframework.stereotype.Component;

@Component
public class ReportHandlerFactory {

    private final RestReportService restReportService;

    public ReportHandlerFactory(RestReportService restReportService) {
        this.restReportService = restReportService;
    }

    public IReportHandler handler(ReportType reportType) {

        switch (reportType) {
            case BALANCE:
                return new ByBalanceReportHandler(restReportService);
            case BY_DATE:
                return new ByDateReportHandler(restReportService);
            case BY_CATEGORY:
                return new ByCategoryReportHandler(restReportService);
            default:
                throw new IllegalStateException("нет реализации типа отчета");
        }
    }
}
