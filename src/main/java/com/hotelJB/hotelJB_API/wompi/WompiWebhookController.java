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

    @PostMapping
    public ResponseEntity<String> handleWompiWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("✅ Webhook recibido de Wompi: " + payload);

        try {
            // Extraer el objeto EnlacePago
            Map<String, Object> enlacePago = (Map<String, Object>) payload.get("EnlacePago");

            String reference = null;
            if (enlacePago != null) {
                reference = (String) enlacePago.get("IdentificadorEnlaceComercio");
            }

            String resultado = (String) payload.get("ResultadoTransaccion");

            System.out.println("Referencia recibida: " + reference);
            System.out.println("Resultado transacción: " + resultado);

            if (reference != null && resultado != null && reference.startsWith("Reserva-")) {
                // Ej. Reserva-12345 → obtenemos el ID
                Integer reservationId = Integer.parseInt(reference.replace("Reserva-", ""));

                Reservation reservation = reservationRepository.findById(reservationId)
                        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

                switch (resultado) {
                    case "ExitosaAprobada" -> {
                        reservation.setStatus("PAGADA");
                        System.out.println("✅ Reserva marcada como PAGADA: " + reservationId);
                    }
                    case "Rechazada" -> {
                        reservation.setStatus("FALLIDA");
                        System.out.println("❌ Reserva marcada como FALLIDA: " + reservationId);
                    }
                    case "Anulada" -> {
                        reservation.setStatus("ANULADA");
                        System.out.println("⚠️ Reserva ANULADA: " + reservationId);
                    }
                    default -> {
                        reservation.setStatus("PENDIENTE");
                        System.out.println("🤔 Estado desconocido. Reserva pendiente: " + reservationId);
                    }
                }

                reservationRepository.save(reservation);
            }

        } catch (Exception e) {
            System.out.println("🚨 Error procesando el webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok("ok"); // evitar reintentos infinitos
        }

        return ResponseEntity.ok("ok");
    }
}
