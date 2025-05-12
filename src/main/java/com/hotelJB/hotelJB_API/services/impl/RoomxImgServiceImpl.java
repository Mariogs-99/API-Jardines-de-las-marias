package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.RoomxImgDTO;
import com.hotelJB.hotelJB_API.models.entities.RoomxImg;
import com.hotelJB.hotelJB_API.repositories.RoomxImgRepository;
import com.hotelJB.hotelJB_API.services.RoomxImgService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomxImgServiceImpl implements RoomxImgService {
    @Autowired
    private RoomxImgRepository roomxImgRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Override
    public void save(RoomxImgDTO data) throws Exception {
        try{
            RoomxImg roomxImgService = new RoomxImg(data.getRoomId(), data.getImgId());
            roomxImgRepository.save(roomxImgService);
        }catch (Exception e){
            throw new Exception("Error save RoomxImg");
        }
    }

    @Override
    public void update(RoomxImgDTO data, int roomxImgId) throws Exception {
        try{
            RoomxImg roomxImg = roomxImgRepository.findById(roomxImgId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "RoomxImg"));

            roomxImg.setRoomId(data.getRoomId());
            roomxImg.setImgId(data.getImgId());

            roomxImgRepository.save(roomxImg);
        }catch (Exception e){
            throw new Exception("Error update room");
        }
    }

    @Override
    public void delete(int roomxImgId) throws Exception {
        try{
            RoomxImg roomxImg = roomxImgRepository.findById(roomxImgId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "RoomxImg"));

            roomxImgRepository.delete(roomxImg);
        }catch (Exception e){
            throw new Exception("Error delete RoomxImg");
        }
    }

    @Override
    public List<RoomxImg> getAll() {
        return roomxImgRepository.findAll();
    }

    @Override
    public Optional<RoomxImg> findById(int roomImgId) {
        return roomxImgRepository.findById(roomImgId);
    }

    @Override
    public List<RoomxImg> findByRoomId(int roomId) {
        return roomxImgRepository.findByRoomId(roomId);
    }
}
