package com.emportexcel.service.Imp;



import com.emportexcel.dto.CustomerInformationDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelParserService {

    public List<CustomerInformationDTO> parseCustomerInformationExcel(MultipartFile file) throws Exception {
        List<CustomerInformationDTO> dtoList = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // bỏ qua dòng tiêu đề

            while (rows.hasNext()) {
                Row row = rows.next();
                CustomerInformationDTO dto = new CustomerInformationDTO();

                dto.setStt(getIntegerCell(row.getCell(0)));
                dto.setFullName(getStringCell(row.getCell(1)));
                dto.setSex(getStringCell(row.getCell(2)));
                dto.setDob(getDateCell(row.getCell(3)));
                dto.setPassportNo(getStringCell(row.getCell(4)));
                dto.setDoi(getDateCell(row.getCell(5)));
                dto.setDoe(getDateCell(row.getCell(6)));

                dtoList.add(dto);
            }
        }

        return dtoList;
    }

    private String getStringCell(Cell cell) {
        if (cell == null) return null;
        return cell.getCellType() == CellType.STRING
                ? cell.getStringCellValue().trim()
                : String.valueOf((long) cell.getNumericCellValue());
    }
    private Integer getIntegerCell(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue(); // chuyển từ double sang int
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private LocalDate getDateCell(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        return null;
    }
}