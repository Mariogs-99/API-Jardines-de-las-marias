package com.hotelJB.hotelJB_API.wompi;

import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook-wompi")
public class WompiWebhookController {

    @Autowired
    private ReservationRepository reservationRepository;

    private static final String RESULT_SUCCESS = "ExitosaAprobada";
    private static final String RESULT_REJECTED = "Rechazada";
    private static final String RESULT_CANCELLED = "Anulada";

    @PostMapping
    public ResponseEntity<String> handleWompiWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("✅ Webhook recibido de Wompi: " + payload);

        try {
            Map<String, Object> enlacePago = (Map<String, Object>) payload.get("EnlacePago");

            String reference = null;
            if (enlacePago != null) {
                reference = (String) enlacePago.get("IdentificadorEnlaceComercio");
            }

            String resultado = (String) payload.get("ResultadoTransaccion");

            System.out.println("Referencia recibida: " + reference);
            System.out.println("Resultado transacción: " + resultado);

            if (reference != null && resultado != null && reference.startsWith("Reserva-")) {
                Integer reservationId;
                try {
                    reservationId = Integer.parseInt(reference.replace("Reserva-", ""));
                } catch (NumberFormatException e) {
                    System.out.println("❌ Identificador inválido en referencia: " + reference);
                    return ResponseEntity.ok("ok");
                }

                Reservation reservation = reservationRepository.findById(reservationId)
                        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

                switch (resultado) {
                    case RESULT_SUCCESS -> {
                        reservation.setStatus("PAGADA");
                        System.out.println("✅ Reserva marcada como PAGADA: " + reservationId);
                    }
                    case RESULT_REJECTED -> {
                        reservation.setStatus("FALLIDA");
                        System.out.println("❌ Reserva marcada como FALLIDA: " + reservationId);
                    }
                    case RESULT_CANCELLED -> {
                        reservation.setStatus("ANULADA");
                        System.out.println("⚠️ Reserva ANULADA: " + reservationId);
                    }
                    default -> {
                        reservation.setStatus("PENDIENTE");
                        System.out.println("🤔 Estado desconocido. Reserva pendiente: " + reservationId);
                    }
                }

                // (Opcional) Guardar código de autorización
                String codigoAutorizacion = (String) payload.get("CodigoAutorizacion");
                if (codigoAutorizacion != null) {
                    System.out.println("Código autorización: " + codigoAutorizacion);
                    // Si tienes campo transactionCode en Reservation, guárdalo
                    // reservation.setTransactionCode(codigoAutorizacion);
                }

                reservationRepository.save(reservation);
            }

        } catch (Exception e) {
            System.out.println("🚨 Error procesando el webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.ok("ok");
    }
}
