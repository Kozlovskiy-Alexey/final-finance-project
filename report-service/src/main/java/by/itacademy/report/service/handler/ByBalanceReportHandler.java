package by.itacademy.report.service.handler;

import by.itacademy.report.dto.AccountDto;
import by.itacademy.report.dto.AccountPageDto;
import by.itacademy.report.dto.RequestParamsDto;
import by.itacademy.report.service.RestReportService;
import by.itacademy.report.service.handler.api.IReportHandler;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ByBalanceReportHandler implements IReportHandler {

    private final RestReportService restReportService;

    public ByBalanceReportHandler(RestReportService restReportService) {
        this.restReportService = restReportService;
    }

    // подгрузить данные с других сервисов по аккаунту и параметрам
    // сложить все данные в workbook и получить byteArray OutputStream
    @Override
    public byte[] handle(RequestParamsDto params) {
        AccountPageDto accountPage = restReportService.getAccountPage();
        List<AccountDto> accounts = accountPage.getContent();

        int rowNumber = 0;

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(rowNumber++);
        row.createCell(0).setCellValue("account_id");
        row.createCell(1).setCellValue("balance");

        List<String> accountsId = params.getAccounts();
        for (String id: accountsId) {
            AccountDto accountDto = restReportService.getAccount(id);
            Row newRow = sheet.createRow(rowNumber++);
            newRow.createCell(0).setCellValue(accountDto.getUuid());
            newRow.createCell(1).setCellValue(accountDto.getBalance());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            wb.write(bos);
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }
}
