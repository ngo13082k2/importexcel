package com.emportexcel.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInformationDTO {
    private Integer stt;
    private String fullName;
    private String sex;
    private String  dob;
    private String passportNo;
    private String  doi;
    private String  doe;
    private String room;
}
