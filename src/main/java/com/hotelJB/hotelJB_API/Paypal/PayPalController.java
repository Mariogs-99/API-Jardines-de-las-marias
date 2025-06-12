package com.hotelJB.hotelJB_API.Paypal;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    private final PayPalClient payPalClient;

    public PayPalController(PayPalClient payPalClient) {
        this.payPalClient = payPalClient;
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
}
