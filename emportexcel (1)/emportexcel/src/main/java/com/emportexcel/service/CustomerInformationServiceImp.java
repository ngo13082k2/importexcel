package com.emportexcel.service;

import com.emportexcel.dto.CustomerInformationDTO;

import java.util.List;

public interface CustomerInformationServiceImp {
    void importFromDTOList(List<CustomerInformationDTO> dtoList);
}
