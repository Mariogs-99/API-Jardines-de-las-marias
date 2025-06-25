package com.hotelJB.hotelJB_API.Paypal;


import com.fasterxml.jackson.databind.JsonNode;
import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    private final PayPalClient payPalClient;
    private final ReservationService reservationService;
    private final PaypalCaptureRepository paypalCaptureRepository;

    public PayPalController(PayPalClient payPalClient, ReservationService reservationService, PaypalCaptureRepository paypalCaptureRepository) {
        this.payPalClient = payPalClient;
        this.reservationService = reservationService;
        this.paypalCaptureRepository = paypalCaptureRepository;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam double total) {
        try {
            String order = payPalClient.createOrder(total);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear orden: " + e.getMessage());
        }
    }




    @PostMapping("/capture-order")
    public ResponseEntity<?> captureOrder(@RequestBody ReservationDTO dto) {
        try {
            String orderId = dto.getPaypalOrderId();

            // Verificar si ya fue capturada antes (opcional pero recomendable)
            if (paypalCaptureRepository.existsByPaypalOrderId(orderId)) {
                return ResponseEntity.badRequest().body("⚠️ Esta orden ya fue capturada.");
            }

            // Obtener detalles de la orden
            JsonNode orderDetails = payPalClient.getOrderDetails(orderId);
            String status = orderDetails.path("status").asText();

            // Si está aprobada, capturamos; si está completada, continuamos
            if ("APPROVED".equalsIgnoreCase(status)) {
                payPalClient.captureOrder(orderId);
            } else if (!"COMPLETED".equalsIgnoreCase(status)) {
                return ResponseEntity.status(422).body("La orden no está en un estado válido. Estado: " + status);
            }

            // Guardar que ya se capturó la orden
            PaypalCapture capture = new PaypalCapture();
            capture.setPaypalOrderId(orderId);
            capture.setCapturedAt(LocalDateTime.now());
            paypalCaptureRepository.save(capture);

            // Guardar la reserva y generar DTE
            ReservationResponse response = reservationService.save(dto);

            return ResponseEntity.ok(Map.of(
                    "message", "Reserva creada y DTE procesado",
                    "reservationCode", response.getReservationCode(),
                    "controlNumber", Optional.ofNullable(response.getDteControlNumber()).orElse("Pendiente")
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error al procesar reserva y DTE: " + e.getMessage());
        }
    }



}
