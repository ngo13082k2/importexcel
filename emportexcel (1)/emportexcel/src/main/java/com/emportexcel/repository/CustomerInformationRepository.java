package com.emportexcel.repository;


import com.emportexcel.model.CustomerInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerInformationRepository extends JpaRepository<CustomerInformation, Long> {
    Optional<CustomerInformation> findByFullNameAndDobAndSex(String fullName, String dob, String sex);

    Optional<CustomerInformation> findByFullNameAndSexAndDob(String fullName, String sex, String dob);
    Optional<CustomerInformation> findByFullNameAndSex(String fullName, String sex);
    Optional<CustomerInformation> findByFullNameAndDob(String fullName, String dob);
    List<CustomerInformation> findByFullName(String fullName);
}
