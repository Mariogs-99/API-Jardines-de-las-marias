package com.hotelJB.hotelJB_API.wompi;

import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.repositories.ReservationRepository;
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
    private TempReservationService tempReservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Endpoint para crear un enlace de pago en Wompi.
     * ✅ Aquí se crea la reserva real (estado PENDIENTE).
     */
    @PostMapping("/wompi/link")
    public ResponseEntity<?> createWompiLink(@RequestBody ReservationDTO reservationDTO) throws Exception {
        String tempReference = "Temp-" + UUID.randomUUID();

        // ✅ Guardar reserva real en BD con estado PENDIENTE
        ReservationResponse savedReservation =
                reservationService.saveWithStatus(reservationDTO, "PENDIENTE");

        String reservationCode = savedReservation.getReservationCode();

        // ✅ Guardar en temp_reservations el DTO con reservationCode
        reservationDTO.setReservationCode(reservationCode);
        tempReservationService.saveTempReservation(tempReference, reservationDTO, Duration.ofHours(24));

        String wompiUrl = wompiService.crearEnlacePago(reservationDTO, tempReference, reservationCode);

        Map<String, Object> response = new HashMap<>();
        response.put("tempReference", tempReference);
        response.put("reservationCode", reservationCode);
        response.put("urlPagoWompi", wompiUrl);

        return ResponseEntity.ok(response);
    }



    /**
     * Endpoint para confirmar reserva manualmente usando tempReference (opcional).
     */
    @GetMapping("/temp-reservations/{tempReference}")
    public ResponseEntity<ReservationResponse> confirmReservation(@PathVariable String tempReference) throws Exception {
        ReservationDTO dto = tempReservationService.getTempReservation(tempReference);

        if (dto == null) {
            throw new RuntimeException("No existe reserva temporal con referencia: " + tempReference);
        }

        if (dto.getReservationCode() == null || dto.getReservationCode().isBlank()) {
            throw new RuntimeException("La reserva temporal no contiene reservationCode.");
        }

        // ✅ Buscar la reserva real por reservationCode
        ReservationResponse existingReservation =
                reservationService.getByReservationCode(dto.getReservationCode());

        if (existingReservation == null) {
            throw new RuntimeException("No se encontró reserva con código: " + dto.getReservationCode());
        }

        return ResponseEntity.ok(existingReservation);
    }

    /**
     * Endpoint para obtener reserva por reservationCode.
     */
    @GetMapping("/by-code/{reservationCode}")
    public ResponseEntity<ReservationResponse> getByCode(@PathVariable String reservationCode) throws Exception {
        ReservationResponse reservation = reservationService.getByReservationCode(reservationCode);
        return ResponseEntity.ok(reservation);
    }

}
