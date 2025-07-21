package com.emportexcel.service.Imp;

import com.emportexcel.dto.CustomerRoomDTO;
import com.emportexcel.dto.CustomerRoomFullInfoDTO;
import com.emportexcel.model.CustomerInformation;
import com.emportexcel.model.CustomerRoom;
import com.emportexcel.repository.CustomerInformationRepository;
import com.emportexcel.repository.CustomerRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        room.setStt(dto.getStt());
        room.setFullName(dto.getFullName());
        room.setSex(dto.getSex());
        room.setDob((dto.getDob()));
        room.setRoom(dto.getRoom());
        return room;
    }

    public List<CustomerRoomFullInfoDTO> getCustomerRoomWithInfo() {
        List<CustomerRoom> rooms = customerRoomRepository.findAll();

        Map<String, List<CustomerRoom>> groupedRooms = rooms.stream()
                .filter(room -> room.getFullName() != null)
                .collect(Collectors.groupingBy(CustomerRoom::getFullName));

        List<CustomerRoomFullInfoDTO> result = new ArrayList<>();

        for (Map.Entry<String, List<CustomerRoom>> entry : groupedRooms.entrySet()) {
            String fullName = entry.getKey();
            List<CustomerRoom> customerRooms = entry.getValue();

            if (customerRooms.size() > 1) {
                String duplicatedSTT = "Khách hàng trùng tên: " + fullName + " ở STT: " +
                        customerRooms.stream()
                                .map(r -> String.valueOf(r.getStt()))
                                .collect(Collectors.joining(", "));

                for (CustomerRoom room : customerRooms) {
                    if (room.getStt() == null) {
                        continue;
                    }

                    CustomerRoomFullInfoDTO dto = new CustomerRoomFullInfoDTO();
                    dto.setFullName(room.getFullName());
                    dto.setSex(null);
                    dto.setDob(null);
                    dto.setRoom(room.getRoom());
                    dto.setPassportNo(null);
                    dto.setDoi(null);
                    dto.setDoe(null);
                    dto.setStt(room.getStt());

                    dto.setDuplicatedSTT(duplicatedSTT);
                    result.add(dto);
                }
            } else {
                for (CustomerRoom room : customerRooms) {
                    if (room.getStt() == null) {
                        continue;
                    }

                    CustomerRoomFullInfoDTO dto = new CustomerRoomFullInfoDTO();
                    dto.setFullName(room.getFullName());
                    dto.setSex(room.getSex());
                    dto.setRoom(room.getRoom());
                    dto.setStt(room.getStt());

                    List<CustomerInformation> customerInfoList = customerInformationRepository.findByFullName(room.getFullName());

                    if (!customerInfoList.isEmpty()) {
                        CustomerInformation info = customerInfoList.get(0);
                        dto.setSex(info.getSex());
                        dto.setDob(info.getDob());
                        dto.setPassportNo(info.getPassportNo());
                        dto.setDoi(info.getDoi());
                        dto.setDoe(info.getDoe());
                    } else {
                        dto.setPassportNo(null);
                        dto.setDoi(null);
                        dto.setDoe(null);
                    }

                    result.add(dto);
                }
            }
        }

        result.sort(Comparator.comparingInt(CustomerRoomFullInfoDTO::getStt));

        return result;
    }










}