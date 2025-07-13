package com.emportexcel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_information")

public class CustomerRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // liên kết 1-1 với bảng customer_information
    @OneToOne
    @JoinColumn(name = "customer_info_id", referencedColumnName = "id")
    private CustomerInformation customerInformation;

    private String room;


}
