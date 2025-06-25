package com.hotelJB.hotelJB_API.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationNotificationDTO {
    private String reservationCode;
    private String name;
    private String initDate;
    private String roomSummary;
}
