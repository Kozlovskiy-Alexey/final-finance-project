package by.itacademy.report.service;

import by.itacademy.report.advice.ResponseError;
import by.itacademy.report.advice.SingleValidateException;
import by.itacademy.report.dto.ReportDto;
import by.itacademy.report.dto.ReportPageDto;
import by.itacademy.report.dto.RequestParamsDto;
import by.itacademy.report.dto.api.ReportStatus;
import by.itacademy.report.dto.util.mapper.ReportDtoToEntityMapper;
import by.itacademy.report.repository.IReportInfoRepository;
import by.itacademy.report.repository.IReportRepository;
import by.itacademy.report.entity.Report;
import by.itacademy.report.entity.ReportInfo;
import by.itacademy.report.service.api.IReportService;
import by.itacademy.report.service.handler.ReportHandlerFactory;
import by.itacademy.report.service.handler.api.IReportHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReportService implements IReportService {

    private final ReportHandlerFactory handlerFactory;
    private final IReportRepository reportRepository;
    private final IReportInfoRepository reportInfoRepository;
    private final ReportDtoToEntityMapper reportDtoToEntityMapper;

    public ReportService(ReportHandlerFactory handlerFactory, IReportRepository reportRepository,
                         IReportInfoRepository reportInfoRepository,
                         ReportDtoToEntityMapper reportDtoToEntityMapper) {
        this.handlerFactory = handlerFactory;
        this.reportRepository = reportRepository;
        this.reportInfoRepository = reportInfoRepository;
        this.reportDtoToEntityMapper = reportDtoToEntityMapper;
    }

    @Override
    public ReportDto create(ReportType reportType, RequestParamsDto params) {
        IReportHandler handler = this.handlerFactory.handler(reportType);
        byte[] text = handler.handle(params);

        Report report = Report.builder()
                .id(UUID.randomUUID().toString())
                .dtCreate(LocalDateTime.now())
                .dtUpdate(LocalDateTime.now())
                .status(ReportStatus.DONE.name())
                .description(reportType.equals(ReportType.BALANCE) ?
                        getDescriptionByBalanceReport(reportType, params) : getDescription(reportType, params))
                .reportType(reportType.name())
                .report(text)
                .build();
        Report entity = reportRepository.save(report);

        for (String account : params.getAccounts()) {
            ReportInfo reportInfo = ReportInfo.builder()
                    .id(UUID.randomUUID().toString())
                    .accountId(account)
                    .reportId(report.getId())
                    .build();
            reportInfoRepository.save(reportInfo);
        }
        return reportDtoToEntityMapper.entityToDto(entity, params, reportType);
    }

    @Override
    public ReportPageDto getReportPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dtUpdate").descending());
        Page<Report> reportPage = reportRepository.findAll(pageable);
        int totalElements = (int) reportPage.getTotalElements();
        int totalPages = reportPage.getTotalPages();
        boolean isFirst = reportPage.isFirst();
        boolean isLast = reportPage.isLast();
        List<Report> reports = reportPage.getContent();
        List<ReportDto> content = new ArrayList<>();

        for (Report report : reports) {
            List<ReportInfo> infos = reportInfoRepository.findAllByAccountIdEqualsAndAccountId(report.getId());
            RequestParamsDto params = new RequestParamsDto();
            List<String> accounts = new ArrayList<>();
            for (ReportInfo r : infos) {
                accounts.add(r.getAccountId());
            }
            params.setAccounts(accounts);
            content.add(ReportDto.builder()
                    .uuid(report.getId())
                    .dtCreate(report.getDtCreate())
                    .dtUpdate(report.getDtUpdate())
                    .status(ReportStatus.DONE)
                    .type(ReportType.valueOf(report.getReportType()))
                    .description(report.getDescription())
                    .params(params)
                    .build());
        }
        return ReportPageDto.builder()
                .number(page)
                .size(size)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .first(isFirst)
                .numberOfElements(reports.size())
                .last(isLast)
                .content(content)
                .build();
    }

    @Override
    public ByteArrayInputStream getReport(String reportId) {
        Optional<Report> isPresent = reportRepository.findById(reportId);
        if (isPresent.isPresent()) {
            byte[] report = isPresent.get().getReport();
            return new ByteArrayInputStream(report);
        } else {
            throw new SingleValidateException(new ResponseError("There is no report with number " + reportId +
                    " in the data base."));
        }
    }

    @Override
    public Optional<Report> checkReport(String reportId) {
        return reportRepository.findById(reportId);
    }
}
