package by.itacademy.report.dto.util.mapper;

import by.itacademy.report.dto.ReportDto;
import by.itacademy.report.dto.RequestParamsDto;
import by.itacademy.report.dto.api.ReportStatus;
import by.itacademy.report.dto.util.mapper.api.IRepositoryMapper;
import by.itacademy.report.entity.Report;
import by.itacademy.report.service.ReportType;
import org.springframework.stereotype.Component;


@Component
public class ReportDtoToEntityMapper implements IRepositoryMapper<Report, ReportDto> {

    @Override
    public ReportDto entityToDto(Report entity, RequestParamsDto params, ReportType reportType) {
        return ReportDto.builder()
                .uuid(entity.getId())
                .dtCreate(entity.getDtCreate())
                .dtUpdate(entity.getDtUpdate())
                .status(ReportStatus.LOADED)
                .type(reportType)
                .description(entity.getDescription())
                .params(params)
                .build();
    }

    @Override
    public Report dtoToEntity(ReportDto dto) {
        return null;
    }
}
