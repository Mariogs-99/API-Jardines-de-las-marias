package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.RoomxRoomDetailDTO;
import com.hotelJB.hotelJB_API.models.entities.RoomxImg;
import com.hotelJB.hotelJB_API.models.entities.RoomxRoomDetail;
import com.hotelJB.hotelJB_API.repositories.RoomxRoomDetailRepository;
import com.hotelJB.hotelJB_API.services.DetailRoomxRoomService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomxRoomDetailServiceimpl implements DetailRoomxRoomService {
    @Autowired
    private RoomxRoomDetailRepository roomxRDetailRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Override
    public void save(RoomxRoomDetailDTO data) throws Exception {
        try{
            RoomxRoomDetail rRDetail = new RoomxRoomDetail(data.getRoomId(), data.getDetailId());
            roomxRDetailRepository.save(rRDetail);
        }catch (Exception e){
            throw new Exception("Error save RRDetail");
        }
    }

    @Override
    public void update(RoomxRoomDetailDTO data, int detailRoomId) throws Exception {
        try{
            RoomxRoomDetail rRDetail = roomxRDetailRepository.findById(detailRoomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "rRDetail"));

            rRDetail.setRoomId(data.getRoomId());
            rRDetail.setDetailRoomId(data.getDetailId());

            roomxRDetailRepository.save(rRDetail);
        }catch (Exception e){
            throw new Exception("Error update room");
        }
    }

    @Override
    public void delete(int detailRoomId) throws Exception {

    }

    @Override
    public List<RoomxRoomDetail> getAll() {
        return List.of();
    }

    @Override
    public Optional<RoomxRoomDetail> findById(int detailRoomId) {
        return Optional.empty();
    }
}
