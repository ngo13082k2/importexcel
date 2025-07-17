package com.emportexcel.service.Imp;

import com.emportexcel.dto.CustomerRoomDTO;
import com.emportexcel.dto.CustomerRoomFullInfoDTO;
import com.emportexcel.model.CustomerInformation;
import com.emportexcel.model.CustomerRoom;
import com.emportexcel.repository.CustomerInformationRepository;
import com.emportexcel.repository.CustomerRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        room.setDob(dto.getDob());
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

            if (customerRooms.size() > 1 && customerRooms.stream().allMatch(room -> room.getSex() == null && room.getDob() == null)) {
                for (CustomerRoom room : customerRooms) {
                    CustomerRoomFullInfoDTO dto = new CustomerRoomFullInfoDTO();
                    dto.setFullName(room.getFullName());
                    dto.setSex(null);
                    dto.setRoom(room.getRoom());
                    dto.setPassportNo(null);
                    dto.setDoi(null);
                    dto.setStt(room.getStt());

                    String duplicatedSTT = "Khách hàng: " + room.getFullName() + " trùng ở STT: " +
                            customerRooms.stream()
                                    .map(r -> String.valueOf(r.getStt()))
                                    .collect(Collectors.joining(", "));
                    dto.setDuplicatedSTT(duplicatedSTT);
                    result.add(dto);
                }
            } else {
                for (CustomerRoom room : customerRooms) {
                    CustomerRoomFullInfoDTO dto = new CustomerRoomFullInfoDTO();
                    dto.setFullName(room.getFullName());
                    dto.setSex(room.getSex());
                    dto.setDob(room.getDob());
                    dto.setRoom(room.getRoom());
                    dto.setStt(room.getStt());

                    List<CustomerInformation> customerInfoList = customerInformationRepository.findByFullName(room.getFullName());
                    if (customerInfoList.size() == 1) {
                        CustomerInformation info = customerInfoList.get(0);
                        if (dto.getSex() == null) {
                            dto.setSex(info.getSex());
                        }
                        if (dto.getDob() == null) {
                            dto.setDob(info.getDob());
                        }
                        dto.setPassportNo(info.getPassportNo());
                        dto.setDoi(info.getDoi());
                        dto.setDoe(info.getDoe());
                    } else if (customerInfoList.size() > 1) {

                        CustomerInformation info = customerInfoList.get(0);
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