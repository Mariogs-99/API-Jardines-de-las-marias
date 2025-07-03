package com.hotelJB.hotelJB_API.wompi;

import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.services.EmailSenderService;
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

    @Autowired
    private EmailSenderService emailSenderService;

    private static final String RESULT_SUCCESS = "ExitosaAprobada";
    private static final String RESULT_REJECTED = "Rechazada";
    private static final String RESULT_CANCELLED = "Anulada";

    @PostMapping
    public ResponseEntity<String> handleWompiWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("✅ Webhook recibido de Wompi: " + payload);

        try {
            Map<String, Object> enlacePago = (Map<String, Object>) payload.get("EnlacePago");

            String tempReference = null;
            if (enlacePago != null) {
                tempReference = (String) enlacePago.get("IdentificadorEnlaceComercio");
            }

            String resultado = (String) payload.get("ResultadoTransaccion");

            System.out.println("Referencia recibida: " + tempReference);
            System.out.println("Resultado transacción: " + resultado);

            if (tempReference != null && resultado != null && tempReference.startsWith("Temp-")) {

                if (RESULT_SUCCESS.equals(resultado)) {
                    ReservationDTO dto = tempReservationService.getTempReservation(tempReference);

                    if (dto != null) {
                        if (dto.getReservationCode() == null || dto.getReservationCode().isBlank()) {
                            System.out.println("❌ DTO temporal no contiene reservationCode.");
                        } else {
                            ReservationResponse reservationResponse =
                                    reservationService.getByReservationCode(dto.getReservationCode());

                            if (reservationResponse != null) {
                                dto.setStatus("ACTIVA");
                                reservationService.update(dto, reservationResponse.getReservationId());

                                System.out.println("✅ Reserva actualizada a ACTIVA: " + dto.getReservationCode());

                                // Enviar el correo tras confirmar reserva
                                Reservation reservationEntity =
                                        reservationService.findById(reservationResponse.getReservationId())
                                                .orElseThrow(() -> new RuntimeException("Reserva no encontrada."));

                                String htmlBody =
                                        reservationService.buildReservationEmailBody(reservationEntity);

                                emailSenderService.sendMail(
                                        reservationEntity.getEmail(),
                                        "Confirmación de Reserva - Hotel Jardines de las Marías",
                                        htmlBody
                                );

                                tempReservationService.deleteTempReservation(tempReference);
                            } else {
                                System.out.println("❌ No se encontró reserva real con código: " + dto.getReservationCode());
                            }
                        }
                    } else {
                        System.out.println("❌ No se encontró temp reservation para referencia: " + tempReference);
                    }

                } else {
                    System.out.println("❌ Pago fallido o anulado. No se crea reserva.");
                }

            } else {
                System.out.println("❌ Referencia inválida o desconocida: " + tempReference);
            }

        } catch (Exception e) {
            System.out.println("🚨 Error procesando el webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok("ok");
        }

        return ResponseEntity.ok("ok");
    }




}
