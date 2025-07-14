package com.emportexcel.repository;


import com.emportexcel.model.CustomerInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CustomerInformationRepository extends JpaRepository<CustomerInformation, Long> {
    Optional<CustomerInformation> findByFullNameAndDobAndSex(String fullName, LocalDate dob, String sex);

}
