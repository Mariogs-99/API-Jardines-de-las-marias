package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.repositories.ReservationRepository;
import com.hotelJB.hotelJB_API.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class AvailabilitySchedulerService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public AvailabilitySchedulerService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    // ⏰ Ejecuta todos los días a las 00:00
  // @Scheduled(cron = "0 0 0 * * *") // Hora del servidor
    @Transactional
    public void updateRoomAvailability() {
        log.info("📆 Iniciando actualización de disponibilidad de habitaciones...");

        List<Room> rooms = roomRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Room room : rooms) {
            int totalQuantity = room.getQuantity() != null ? room.getQuantity() : 0;
            int reserved = reservationRepository.countReservedQuantityByRoomAndDates(room, today, today);

            int available = totalQuantity - reserved;
            room.setAvailableQuantity(Math.max(available, 0)); // Siempre evita negativos

            log.info("🛏️ Room ID {}: {} total, {} reservadas, {} disponibles",
                    room.getRoomId(), totalQuantity, reserved, room.getAvailableQuantity());
        }

        roomRepository.saveAll(rooms);
        log.info("✅ Disponibilidad de habitaciones actualizada correctamente.");
    }
}
