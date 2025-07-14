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
public class CustomerRoomFullInfoDTO {
    private String fullName;
    private String sex;
    private LocalDate dob;
    private String room;

    private String passportNo;
    private LocalDate doi;
    private LocalDate doe;

}
