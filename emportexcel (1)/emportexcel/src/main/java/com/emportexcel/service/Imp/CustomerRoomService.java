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
        // Lấy tất cả các khách hàng từ bảng CustomerRoom
        List<CustomerRoom> rooms = customerRoomRepository.findAll();

        // Nhóm các khách hàng theo tên (fullName)
        Map<String, List<CustomerRoom>> groupedRooms = rooms.stream()
                .filter(room -> room.getFullName() != null)  // Chỉ xử lý các khách hàng có tên
                .collect(Collectors.groupingBy(CustomerRoom::getFullName));

        List<CustomerRoomFullInfoDTO> result = new ArrayList<>();

        // Duyệt qua từng nhóm khách hàng đã được nhóm theo tên
        for (Map.Entry<String, List<CustomerRoom>> entry : groupedRooms.entrySet()) {
            String fullName = entry.getKey();  // Lấy tên khách hàng
            List<CustomerRoom> customerRooms = entry.getValue();

            // Nếu có nhiều khách hàng trùng tên
            if (customerRooms.size() > 1) {
                // Tạo thông báo về việc trùng tên và các số thứ tự STT
                String duplicatedSTT = "Khách hàng trùng tên: " + fullName + " ở STT: " +
                        customerRooms.stream()
                                .map(r -> String.valueOf(r.getStt()))  // Lấy số thứ tự STT của khách hàng trùng
                                .collect(Collectors.joining(", "));

                // Lặp qua các khách hàng trong nhóm trùng tên và để trống tất cả thông tin
                for (CustomerRoom room : customerRooms) {
                    // Kiểm tra nếu STT là null, bỏ qua khách hàng này
                    if (room.getStt() == null) {
                        continue;  // Bỏ qua khách hàng này nếu STT là null
                    }

                    CustomerRoomFullInfoDTO dto = new CustomerRoomFullInfoDTO();
                    dto.setFullName(room.getFullName());
                    dto.setSex(null);  // Để trống giới tính
                    dto.setDob(null);  // Để trống ngày sinh
                    dto.setPassportNo(null);  // Để trống số hộ chiếu
                    dto.setDoi(null);  // Để trống ngày cấp
                    dto.setDoe(null);  // Để trống ngày hết hạn
                    dto.setStt(room.getStt());  // Giữ lại số thứ tự
                    dto.setRoom(room.getRoom());  // Giữ lại thông tin phòng

                    dto.setDuplicatedSTT(duplicatedSTT);  // Ghi rõ thông tin trùng tên và các STT
                    result.add(dto);
                }
            } else {
                // Trường hợp chỉ có một khách hàng, lấy tất cả thông tin từ bảng CustomerInformation
                for (CustomerRoom room : customerRooms) {
                    // Kiểm tra nếu STT là null, bỏ qua khách hàng này
                    if (room.getStt() == null) {
                        continue;  // Bỏ qua khách hàng này nếu STT là null
                    }

                    CustomerRoomFullInfoDTO dto = new CustomerRoomFullInfoDTO();
                    dto.setFullName(room.getFullName());
                    dto.setSex(room.getSex());
                    dto.setRoom(room.getRoom());
                    dto.setStt(room.getStt());

                    // Tìm kiếm thông tin khách hàng trong bảng CustomerInformation
                    List<CustomerInformation> customerInfoList = customerInformationRepository.findByFullName(room.getFullName());

                    if (!customerInfoList.isEmpty()) {
                        CustomerInformation info = customerInfoList.get(0);
                        dto.setSex(info.getSex());
                        dto.setDob(info.getDob());
                        dto.setPassportNo(info.getPassportNo());
                        dto.setDoi(info.getDoi());
                        dto.setDoe(info.getDoe());
                    } else {
                        // Nếu không có thông tin trong bảng CustomerInformation, để null các trường
                        dto.setPassportNo(null);
                        dto.setDoi(null);
                        dto.setDoe(null);
                    }

                    result.add(dto);
                }
            }
        }

        // Sắp xếp theo STT từ nhỏ đến lớn
        result.sort(Comparator.comparingInt(CustomerRoomFullInfoDTO::getStt));

        return result;
    }










}