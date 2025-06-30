package com.hotelJB.hotelJB_API.wompi;

import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook-wompi")
public class WompiWebhookController {

    @Autowired
    private TempReservationService tempReservationService;

    @Autowired
    private ReservationService reservationService;

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

            if (reference != null && resultado != null && reference.startsWith("Temp-")) {

                // 🔶 SOLO SI ES EXITOSA
                if (RESULT_SUCCESS.equals(resultado)) {
                    // Recuperar DTO temporal
                    ReservationDTO dto = tempReservationService.getTempReservation(reference);

                    if (dto != null) {
                        // Guardar reserva real en BD
                        ReservationResponse saved = reservationService.save(dto);

                        System.out.println("✅ Reserva creada tras pago exitoso. ID: " + saved.getReservationId());

                        // Eliminar DTO temporal
                        tempReservationService.deleteTempReservation(reference);
                    } else {
                        System.out.println("❌ No se encontró pre-reserva para referencia: " + reference);
                    }
                } else {
                    System.out.println("❌ Pago fallido o anulado. No se crea reserva.");
                }
            } else {
                System.out.println("❌ Referencia inválida o desconocida: " + reference);
            }

        } catch (Exception e) {
            System.out.println("🚨 Error procesando el webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.ok("ok");
    }
}
