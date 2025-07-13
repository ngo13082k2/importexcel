package com.emportexcel.controller;

import com.emportexcel.dto.CustomerInformationDTO;
import com.emportexcel.service.CustomerInformationServiceImp;
import com.emportexcel.service.Imp.CustomerInformationService;
import com.emportexcel.service.Imp.ExcelParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/customer-information")
public class CustomerInformationController {

    @Autowired
    private ExcelParserService excelParserService;

    @Autowired
    private CustomerInformationServiceImp customerInformationService;

    @PostMapping("/import")
    public ResponseEntity<String> importCustomerInformation(@RequestParam("file") MultipartFile file) {
        try {
            List<CustomerInformationDTO> dtoList = excelParserService.parseCustomerInformationExcel(file);
            customerInformationService.importFromDTOList(dtoList);
            return ResponseEntity.ok("Import thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Import thất bại: " + e.getMessage());
        }
    }
}