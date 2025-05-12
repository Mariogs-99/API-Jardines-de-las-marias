package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.DetailRoomDTO;
import com.hotelJB.hotelJB_API.models.entities.DetailRoom;
import com.hotelJB.hotelJB_API.models.responses.DetailRoomResponse;
import com.hotelJB.hotelJB_API.repositories.DetailRoomRepository;
import com.hotelJB.hotelJB_API.services.DetailRoomService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetailRoomServiceImpl implements DetailRoomService {
    @Autowired
    private DetailRoomRepository detailRoomRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Override
    public void save(DetailRoomDTO data) throws Exception {
        try{
            DetailRoom detailRoom = new DetailRoom(data.getDetailNameEs(), data.getDetailNameEn(),data.getIcon());
            detailRoomRepository.save(detailRoom);
        }catch (Exception e){
            throw new Exception("Error save Detail Room");
        }
    }

    @Override
    public void update(DetailRoomDTO data, int detailRoomId) throws Exception {
        try{
            DetailRoom detailRoom = detailRoomRepository.findById(detailRoomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "DetailRoom"));

            detailRoom.setDetailNameEs(data.getDetailNameEs());
            detailRoom.setDetailNameEn(data.getDetailNameEn());
            detailRoom.setIcon(data.getIcon());

            detailRoomRepository.save(detailRoom);
        }catch (Exception e){
            throw new Exception("Error update Detail Room");
        }
    }

    @Override
    public void delete(int detailRoomId) throws Exception {
        try{
            DetailRoom detailRoom = detailRoomRepository.findById(detailRoomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "DetailRoom"));

            detailRoomRepository.delete(detailRoom);
        }catch (Exception e){
            throw new Exception("Error delete contact");
        }
    }

    @Override
    public List<DetailRoom> getAll() {
        return detailRoomRepository.findAll();
    }

    @Override
    public Optional<DetailRoomResponse> findById(int detailRoomId, String lang) {
        Optional<DetailRoom> detailRoom = detailRoomRepository.findById(detailRoomId);

        return detailRoom.map(value -> new DetailRoomResponse(
                value.getDetailRoomId(),
                "es".equals(lang) ? value.getDetailNameEs() : value.getDetailNameEn(),
                value.getIcon()
        ));
    }

    @Override
    public List<DetailRoomResponse> findByLanguage(String language) {
        List<DetailRoom> detailRoom = detailRoomRepository.findAll();

        return detailRoom.stream().map(value -> new DetailRoomResponse(
                value.getDetailRoomId(),
                "es".equals(language) ? value.getDetailNameEs() : value.getDetailNameEn(),
                value.getIcon()
        )).toList();

    }
}
