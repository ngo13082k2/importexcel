package com.emportexcel.service.Imp;

import com.emportexcel.dto.CustomerRoomDTO;
import com.emportexcel.dto.CustomerRoomFullInfoDTO;
import com.emportexcel.model.CustomerInformation;
import com.emportexcel.model.CustomerRoom;
import com.emportexcel.repository.CustomerInformationRepository;
import com.emportexcel.repository.CustomerRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerRoomService {

    @Autowired
    private CustomerRoomRepository customerRoomRepository;
    @Autowired
    private CustomerInformationRepository customerInformationRepository;

    public void importRooms(List<CustomerRoomDTO> dtoList) {
        List<CustomerRoom> rooms = dtoList.stream()
                .map(this::mapToEntity)
                .toList();

        customerRoomRepository.saveAll(rooms);
    }

    private CustomerRoom mapToEntity(CustomerRoomDTO dto) {
        CustomerRoom room = new CustomerRoom();
        room.setFullName(dto.getFullName());
        room.setSex(dto.getSex());
        room.setDob(dto.getDob());
        room.setRoom(dto.getRoom());
        return room;
    }

    public List<CustomerRoomFullInfoDTO> getAllRoomWithPassportInfo() {
        List<CustomerRoom> rooms = customerRoomRepository.findAll();

        return rooms.stream().map(room -> {
            CustomerRoomFullInfoDTO dto = new CustomerRoomFullInfoDTO();

            dto.setFullName(room.getFullName());
            dto.setSex(room.getSex());
            dto.setDob(room.getDob());
            dto.setRoom(room.getRoom());

            customerInformationRepository
                    .findByFullNameAndDobAndSex(room.getFullName(), room.getDob(), room.getSex())
                    .ifPresent(info -> {
                        dto.setPassportNo(info.getPassportNo());
                        dto.setDoi(info.getDoi());
                        dto.setDoe(info.getDoe());
                    });

            return dto;
        }).toList();
    }
}