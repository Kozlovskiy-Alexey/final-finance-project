package by.itacademy.report.dto.util.mapper.api;

import by.itacademy.report.dto.RequestParamsDto;
import by.itacademy.report.service.ReportType;

public interface IRepositoryMapper<E, D> {

    D entityToDto(E entity, RequestParamsDto params, ReportType reportType);
    E dtoToEntity(D dto);
}
