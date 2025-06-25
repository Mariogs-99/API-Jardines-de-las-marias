package com.hotelJB.hotelJB_API.Paypal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaypalCaptureRepository extends JpaRepository<PaypalCapture, Long> {
    boolean existsByPaypalOrderId(String paypalOrderId);
}
