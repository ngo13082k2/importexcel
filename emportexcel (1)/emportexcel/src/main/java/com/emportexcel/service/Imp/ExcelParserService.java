package com.emportexcel.service.Imp;



import com.emportexcel.dto.CustomerInformationDTO;
import com.emportexcel.dto.CustomerRoomDTO;
import com.emportexcel.dto.CustomerRoomFullInfoDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelParserService {
    @Autowired
    private CustomerRoomService customerRoomService;


    public List<CustomerInformationDTO> parseCustomerInformationExcel(MultipartFile file) throws Exception {
        List<CustomerInformationDTO> dtoList = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next();

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

        if (cell.getCellType() == CellType.STRING) {
            try {
                return LocalDate.parse(cell.getStringCellValue().trim());
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }
    public List<CustomerRoomDTO> parseCustomerRoomExcel(MultipartFile file) throws Exception {
        List<CustomerRoomDTO> dtoList = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // bỏ qua dòng tiêu đề

            while (rows.hasNext()) {
                Row row = rows.next();
                CustomerRoomDTO dto = new CustomerRoomDTO();

                dto.setStt(getIntegerCell(row.getCell(0)));
                dto.setFullName(getStringCell(row.getCell(1)));  // FullName - cột B
                dto.setSex(getStringCell(row.getCell(2)));       // Sex - cột C
                dto.setDob(getDateCell(row.getCell(3)));         // DOB - cột D
                dto.setRoom(getStringCell(row.getCell(4)));      // Room - cột E

                dtoList.add(dto);
            }
        }

        return dtoList;
    }
    public ByteArrayInputStream exportToExcel(List<CustomerRoomFullInfoDTO> data) throws IOException {
        String[] headers = {"Full Name", "Sex", "DOB", "Room", "Passport No", "DOI", "DOE"};

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Customer Room Full Info");

            // Header
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Data
            int rowIdx = 1;
            for (CustomerRoomFullInfoDTO dto : data) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(dto.getFullName());
                row.createCell(1).setCellValue(dto.getSex());
                row.createCell(2).setCellValue(dto.getDob() != null ? dto.getDob().toString() : "");
                row.createCell(3).setCellValue(dto.getRoom());
                row.createCell(4).setCellValue(dto.getPassportNo() != null ? dto.getPassportNo() : "");
                row.createCell(5).setCellValue(dto.getDoi() != null ? dto.getDoi().toString() : "");
                row.createCell(6).setCellValue(dto.getDoe() != null ? dto.getDoe().toString() : "");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }




}