package com.emportexcel.controller;

import com.emportexcel.dto.CustomerRoomDTO;
import com.emportexcel.dto.CustomerRoomFullInfoDTO;
import com.emportexcel.service.Imp.CustomerRoomService;
import com.emportexcel.service.Imp.ExcelParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/customer-room")
public class CustomerRoomController {

    @Autowired
    private ExcelParserService excelParserService;

    @Autowired
    private CustomerRoomService customerRoomService;


    @PostMapping("/import")
    public ResponseEntity<String> importRoomExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<CustomerRoomDTO> roomDtos = excelParserService.parseCustomerRoomExcel(file);
            customerRoomService.importRooms(roomDtos);
            return ResponseEntity.ok("Import room thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi import room: " + e.getMessage());
        }
    }
    @GetMapping("/get-all-field")
    public ResponseEntity<List<CustomerRoomFullInfoDTO>> getAllFullRoomInfo() {
        List<CustomerRoomFullInfoDTO> result = customerRoomService.getAllRoomWithPassportInfo();
        return ResponseEntity.ok(result);
    }
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportCustomerRoomToExcel() {
        try {
            List<CustomerRoomFullInfoDTO> data = customerRoomService.getAllRoomWithPassportInfo();
            ByteArrayInputStream in = excelParserService.exportToExcel(data);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=customer_room.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new InputStreamResource(in));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}