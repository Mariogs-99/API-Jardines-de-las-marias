package com.hotelJB.hotelJB_API.websocket;

import com.hotelJB.hotelJB_API.models.entities.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class WebSocketNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyNewReservation(Reservation reservation) {
        String roomName = reservation.getRoom() != null ? reservation.getRoom().getNameEs() : "Sin habitación";

        ReservationNotificationDTO dto = new ReservationNotificationDTO(
                reservation.getReservationCode(),
                reservation.getName(),
                reservation.getInitDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                roomName + " × " + reservation.getQuantityReserved()
        );

        messagingTemplate.convertAndSend("/topic/reservations", dto);
    }
}
