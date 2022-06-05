package by.itacademy.report.service.handler;

import by.itacademy.report.advice.ResponseError;
import by.itacademy.report.advice.SingleValidateException;
import by.itacademy.report.dto.CategoryDto;
import by.itacademy.report.dto.CategoryPageDto;
import by.itacademy.report.dto.CurrencyDto;
import by.itacademy.report.dto.CurrencyPageDto;
import by.itacademy.report.dto.OperationDto;
import by.itacademy.report.dto.OperationPageDto;
import by.itacademy.report.dto.RequestParamsDto;
import by.itacademy.report.service.RestReportService;
import by.itacademy.report.service.handler.api.IReportHandler;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ByDateReportHandler implements IReportHandler {

    private final RestReportService restReportService;

    public ByDateReportHandler(RestReportService restReportService) {
        this.restReportService = restReportService;
    }

    @Override
    public byte[] handle(RequestParamsDto params) {

        CategoryPageDto categoryPage = restReportService.getCategoryPage();
        List<CategoryDto> categoriesDto = categoryPage.getContent();
        Map<String, String> categories = new HashMap<>();
        for (CategoryDto dto : categoriesDto) {
            categories.put(dto.getId(), dto.getTitle());
        }

        CurrencyPageDto currencyPage = restReportService.getCurrencyPage();
        List<CurrencyDto> currencyDto = currencyPage.getContent();
        Map<String, String> currencies = new HashMap<>();
        for (CurrencyDto dto : currencyDto) {
            currencies.put(dto.getId(), dto.getTitle());
        }

        List<String> accounts = params.getAccounts();
        LocalDateTime from = params.getFrom();
        LocalDateTime to = params.getTo();
        List<String> categoriesList = params.getCategories();

        Workbook wb = new XSSFWorkbook();

        for (String account : accounts) {
            OperationPageDto operationPageDto = restReportService.getOperationPage(account);
            List<OperationDto> operationDtoList = operationPageDto.getContent();

            // filter by date
            operationDtoList.removeIf(dto -> !dto.getDate().isAfter(from) && !dto.getDate().isBefore(to));

            // filter by category
            List<OperationDto> filteredList = new ArrayList<>();
            for (String category : categoriesList) {
               for (OperationDto dto: operationDtoList) {
                   if (dto.getOperationCategory().equals(category)) {
                       filteredList.add(dto);
                   }
               }
            }
            filteredList.sort(Comparator.comparing(OperationDto::getDate)
                    .thenComparing(OperationDto::getOperationCategory));

            Sheet sheet = wb.createSheet(account.substring(24));
            int rowNumber = 0;
            Row row = sheet.createRow(rowNumber++);

            row.createCell(0).setCellValue("date");
            row.createCell(1).setCellValue("value");
            row.createCell(2).setCellValue("category");
            row.createCell(3).setCellValue("currency");
            row.createCell(4).setCellValue("description");

            for (OperationDto dto : filteredList) {
                Row newRow = sheet.createRow(rowNumber++);
                newRow.createCell(0).setCellValue(dto.getDate().toString());
                newRow.createCell(1).setCellValue(dto.getOperationValue());
                newRow.createCell(2).setCellValue(categories.get(dto.getOperationCategory()));
                newRow.createCell(3).setCellValue(currencies.get(dto.getCurrencyUuid()));
                newRow.createCell(4).setCellValue(dto.getDescription());
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            wb.write(bos);
            wb.close();
        } catch (IOException e) {
            throw new SingleValidateException(new ResponseError("error writing report to ByteArrayOutputStream"));
        }
        return bos.toByteArray();
    }
}
