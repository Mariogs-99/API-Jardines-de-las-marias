package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.RoomxDetailDTO;
import com.hotelJB.hotelJB_API.models.entities.RoomxRoomDetail;
import com.hotelJB.hotelJB_API.repositories.RoomxRoomDetailRepository;
import com.hotelJB.hotelJB_API.services.RoomxDetailService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomxDetailServiceImpl implements RoomxDetailService {
    @Autowired
    private RoomxRoomDetailRepository roomxDetailRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Override
    public void save(RoomxDetailDTO data) throws Exception {
        try{
            RoomxRoomDetail roomxDetail = new RoomxRoomDetail(data.getRoomId(), data.getDetailRoomId());
            roomxDetailRepository.save(roomxDetail);
        }catch (Exception e){
            throw new Exception("Error save RoomxDetail");
        }
    }

    @Override
    public void update(RoomxDetailDTO data, int roomxDetailId) throws Exception {
        try{
            RoomxRoomDetail roomxDetail = roomxDetailRepository.findById(roomxDetailId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "RoomxDetail"));

            roomxDetail.setRoomId(data.getRoomId());
            roomxDetail.setDetailRoomId(data.getDetailRoomId());

            roomxDetailRepository.save(roomxDetail);
        }catch (Exception e){
            throw new Exception("Error update RoomxDetail");
        }
    }

    @Override
    public void delete(int roomxDetailId) throws Exception {
        try{
            RoomxRoomDetail roomxDetail = roomxDetailRepository.findById(roomxDetailId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "RoomxDetail"));

            roomxDetailRepository.delete(roomxDetail);
        }catch (Exception e){
            throw new Exception("Error delete RoomxImg");
        }
    }

    @Override
    public List<RoomxRoomDetail> getAll() {
        return roomxDetailRepository.findAll();
    }

    @Override
    public Optional<RoomxRoomDetail> findById(int roomxDetailId) {
        return roomxDetailRepository.findById(roomxDetailId);
    }
}