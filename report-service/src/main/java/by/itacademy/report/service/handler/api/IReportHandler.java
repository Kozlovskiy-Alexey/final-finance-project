package by.itacademy.report.service.handler.api;

import by.itacademy.report.dto.RequestParamsDto;

public interface IReportHandler {
    byte[] handle(RequestParamsDto params);
}
