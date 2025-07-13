package com.emportexcel.service.Imp;

import com.emportexcel.dto.CustomerInformationDTO;
import com.emportexcel.model.CustomerInformation;
import com.emportexcel.repository.CustomerInformationRepository;
import com.emportexcel.service.CustomerInformationServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerInformationService implements CustomerInformationServiceImp {

    @Autowired
    private CustomerInformationRepository repository;

    public void importFromDTOList(List<CustomerInformationDTO> dtoList) {
        List<CustomerInformation> entities = dtoList.stream()
                .map(this::mapToEntity)
                .toList();
        repository.saveAll(entities);
    }

    private CustomerInformation mapToEntity(CustomerInformationDTO dto) {
        CustomerInformation entity = new CustomerInformation();
        entity.setFullName(dto.getFullName());
        entity.setSex(dto.getSex());
        entity.setDob(dto.getDob());
        entity.setPassportNo(dto.getPassportNo());
        entity.setDoi(dto.getDoi());
        entity.setDoe(dto.getDoe());
        return entity;
    }

    public CustomerInformationDTO mapToDTO(CustomerInformation entity) {
        CustomerInformationDTO dto = new CustomerInformationDTO();
        dto.setFullName(entity.getFullName());
        dto.setSex(entity.getSex());
        dto.setDob(entity.getDob());
        dto.setPassportNo(entity.getPassportNo());
        dto.setDoi(entity.getDoi());
        dto.setDoe(entity.getDoe());
        return dto;
    }
}