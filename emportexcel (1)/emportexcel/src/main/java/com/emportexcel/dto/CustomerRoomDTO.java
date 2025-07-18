package com.emportexcel.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRoomDTO {
    private Integer stt;
    private String fullName;
    private String sex;
    private String  dob;
    private String room;

    private String passportNo;
    private String  doi;
    private String  doe;
}