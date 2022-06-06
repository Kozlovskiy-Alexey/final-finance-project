package by.itacademy.report.controller;

import by.itacademy.report.dto.ReportDto;
import by.itacademy.report.dto.ReportPageDto;
import by.itacademy.report.dto.RequestParamsDto;
import by.itacademy.report.service.ReportService;
import by.itacademy.report.service.ReportType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/{type}")
    public ResponseEntity<ReportDto> create(@PathVariable(name = "type") ReportType reportType,
                                            @RequestBody RequestParamsDto params) {
        ReportDto dto = reportService.create(reportType, params);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ReportPageDto> getReportPage(@RequestParam(name = "page", defaultValue = "1") int page,
                                                       @RequestParam(name = "size", defaultValue = "100") int size) {
        ReportPageDto dto = reportService.getReportPage(page, size);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{uuid}/export")
    public ResponseEntity<Resource> downloadReport(@PathVariable(name = "uuid") String reportId) {
        String fileName = "report.xlsx";
        InputStreamResource file = new InputStreamResource(reportService.getReport(reportId));
        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @RequestMapping(method = RequestMethod.HEAD, value ={"", "/", "/{uuid}"})
    public ResponseEntity<String> checkAvailableReport(@PathVariable(name = "uuid", required = false) String reportId) {
        if (reportId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.println(reportId);
        boolean isPresent = reportService.checkReport(reportId).isPresent();
        return isPresent ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
