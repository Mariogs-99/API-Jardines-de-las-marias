package com.hotelJB.hotelJB_API.wompi;

import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
public class WompiController {

    @Autowired
    private WompiService wompiService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TempReservationService tempReservationService; // ⚠️ servicio para almacenamiento temporal

    @PostMapping("/wompi/link")
    public ResponseEntity<?> createWompiLink(@RequestBody ReservationDTO reservationDTO) throws Exception {
        // ✅ Generar un tempReference único
        String tempReference = "Temp-" + UUID.randomUUID().toString();

        // ✅ Guardar DTO temporalmente (en Redis, DB temporal, etc.)
        tempReservationService.saveTempReservation(tempReference, reservationDTO, Duration.ofHours(24));

        // ✅ Generar link de pago en Wompi
        String wompiUrl = wompiService.crearEnlacePago(reservationDTO, tempReference);

        Map<String, Object> response = new HashMap<>();
        response.put("tempReference", tempReference);
        response.put("urlPagoWompi", wompiUrl);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/temp-reservations/{tempReference}")
    public ResponseEntity<ReservationResponse> confirmReservation(@PathVariable String tempReference) throws Exception {
        ReservationResponse reservation = tempReservationService.confirmReservation(tempReference);
        return ResponseEntity.ok(reservation);
    }


}
