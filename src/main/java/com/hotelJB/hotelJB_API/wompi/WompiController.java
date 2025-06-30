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

        // Guardar la reserva y obtener el response DTO
        ReservationResponse savedReservation = reservationService.save(reservationDTO);

        // Buscar la entidad Reservation en la BD
        Reservation reservationEntity = reservationRepository.findById(savedReservation.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Crear el enlace de pago en Wompi
        String wompiUrl = wompiService.crearEnlacePago(reservationEntity);

        Map<String, Object> response = new HashMap<>();
        response.put("reservationId", savedReservation.getReservationId());
        response.put("reservationCode", savedReservation.getReservationCode());
        response.put("urlPagoWompi", wompiUrl);

        return ResponseEntity.ok(response);
    }
}
