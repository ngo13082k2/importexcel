package com.emportexcel.repository;


import com.emportexcel.model.CustomerInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerInformationRepository extends JpaRepository<CustomerInformation, Long> {
}
