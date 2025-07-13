package com.emportexcel.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_room")

public class CustomerInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String sex;
    private LocalDate dob;
    private String passportNo;
    private LocalDate doi;
    private LocalDate doe;
    @OneToOne(mappedBy = "customerInformation", cascade = CascadeType.ALL)
    private CustomerRoom customerRoom;


}
