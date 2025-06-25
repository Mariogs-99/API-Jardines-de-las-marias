package com.hotelJB.hotelJB_API.Paypal;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PaypalCapture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paypalOrderId;

    private LocalDateTime capturedAt;
}
