package com.hotelJB.hotelJB_API.wompi;

import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.repositories.ReservationRepository;
import com.hotelJB.hotelJB_API.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class WompiController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private WompiService wompiService;

    @PostMapping("/wompi")
    public ResponseEntity<?> createReservationWithWompi(@RequestBody ReservationDTO reservationDTO) throws Exception {

        ReservationResponse savedReservation = reservationService.save(reservationDTO);

        String wompiUrl = wompiService.crearEnlacePago(savedReservation);

        Map<String, Object> response = new HashMap<>();
        response.put("reservationId", savedReservation.getReservationId());
        response.put("reservationCode", savedReservation.getReservationCode());
        response.put("urlPagoWompi", wompiUrl);

        return ResponseEntity.ok(response);
    }


}
