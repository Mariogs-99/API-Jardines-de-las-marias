package com.hotelJB.hotelJB_API.wompi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.sql.Timestamp;

@Service
public class TempReservationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationService reservationService;

    public void saveTempReservation(String tempReference, ReservationDTO dto, Duration ttl) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            LocalDateTime expiration = LocalDateTime.now().plus(ttl);

            jdbcTemplate.update(
                    "INSERT INTO temp_reservations (temp_reference, reservation_json, expiration) " +
                            "VALUES (?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE reservation_json = VALUES(reservation_json), expiration = VALUES(expiration)",
                    tempReference, json, Timestamp.valueOf(expiration)
            );

            System.out.println("✅ TempReservation guardada en BD con referencia: " + tempReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar ReservationDTO a JSON", e);
        }
    }

    public ReservationDTO getTempReservation(String tempReference) {
        try {
            String sql = """
                SELECT reservation_json, expiration
                FROM temp_reservations
                WHERE temp_reference = ?
                """;

            return jdbcTemplate.query(sql, rs -> {
                if (rs.next()) {
                    LocalDateTime expiration = rs.getTimestamp("expiration").toLocalDateTime();
                    if (expiration.isBefore(LocalDateTime.now())) {
                        deleteTempReservation(tempReference);
                        System.out.println("⚠️ TempReservation expiró y fue eliminada: " + tempReference);
                        return null;
                    }
                    String json = rs.getString("reservation_json");
                    ReservationDTO dto = null;
                    try {
                        dto = objectMapper.readValue(json, ReservationDTO.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("✅ TempReservation recuperada desde BD: " + tempReference);
                    return dto;
                } else {
                    System.out.println("⚠️ No se encontró TempReservation en BD: " + tempReference);
                    return null;
                }
            }, tempReference);
        } catch (Exception e) {
            throw new RuntimeException("Error al recuperar ReservationDTO desde la BD", e);
        }
    }

    public void deleteTempReservation(String tempReference) {
        jdbcTemplate.update(
                "DELETE FROM temp_reservations WHERE temp_reference = ?",
                tempReference
        );
        System.out.println("🗑️ TempReservation eliminada de BD: " + tempReference);
    }

    /**
     * Confirma la reserva existente. No crea una nueva.
     */
    public ReservationResponse confirmReservation(String tempReference) throws Exception {
        ReservationDTO dto = getTempReservation(tempReference);
        if (dto == null) {
            throw new RuntimeException("No existe reserva temporal con referencia: " + tempReference);
        }

        if (dto.getReservationCode() == null || dto.getReservationCode().isBlank()) {
            throw new RuntimeException("La reserva temporal no contiene reservationCode.");
        }

        ReservationResponse existingReservation =
                reservationService.getByReservationCode(dto.getReservationCode());
        if (existingReservation == null) {
            throw new RuntimeException("No se encontró reserva definitiva con código: " + dto.getReservationCode());
        }

        // Si quisieras actualizar algo (p.ej. status), puedes hacerlo así:
        /*
        existingReservation.setStatus("ACTIVA");
        reservationService.update(...);
        */

        deleteTempReservation(tempReference);

        System.out.println("✅ Reserva definitiva confirmada con código: " + existingReservation.getReservationCode());
        return existingReservation;
    }
}
