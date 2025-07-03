package com.hotelJB.hotelJB_API.services.impl;
import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import com.hotelJB.hotelJB_API.models.dtos.ReservationRoomDTO;
import com.hotelJB.hotelJB_API.models.entities.Reservation;
import com.hotelJB.hotelJB_API.models.entities.ReservationRoom;
import com.hotelJB.hotelJB_API.models.entities.Room;
import com.hotelJB.hotelJB_API.models.responses.CategoryRoomResponse;
import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import com.hotelJB.hotelJB_API.models.responses.ReservationRoomResponse;
import com.hotelJB.hotelJB_API.models.responses.RoomResponse;
import com.hotelJB.hotelJB_API.repositories.ReservationRepository;
import com.hotelJB.hotelJB_API.repositories.ReservationRoomRepository;
import com.hotelJB.hotelJB_API.repositories.RoomRepository;
import com.hotelJB.hotelJB_API.services.EmailSenderService;
import com.hotelJB.hotelJB_API.services.ReservationRoomService;
import com.hotelJB.hotelJB_API.services.ReservationService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.websocket.WebSocketNotificationService;
import com.hotelJB.hotelJB_API.wompi.WompiService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRoomRepository reservationRoomRepository;

    @Autowired
    private ReservationRoomService reservationRoomService;

    @Autowired
    private EmailSenderService gmailSenderService;

    @Autowired
    private EmailSenderService emailSenderService;


    @Autowired
    private WebSocketNotificationService webSocketNotificationService;

    @Autowired
    private WompiService wompiService;




    @Override
    public ReservationResponse save(ReservationDTO data) throws Exception {
        int totalReserved = data.getRooms().stream()
                .mapToInt(ReservationRoomDTO::getQuantity)
                .sum();

        Reservation reservation = new Reservation(
                data.getInitDate(),
                data.getFinishDate(),
                data.getCantPeople(),
                data.getName(),
                data.getEmail(),
                data.getPhone(),
                data.getPayment(),
                null,
                totalReserved
        );

        String initialStatus = data.getInitDate().isAfter(LocalDate.now()) ? "FUTURA" : "ACTIVA";
        reservation.setStatus(initialStatus);
        reservation.setRoomNumber(data.getRoomNumber());

        if (!data.getRooms().isEmpty()) {
            int firstRoomId = data.getRooms().get(0).getRoomId();
            Room room = roomRepository.findById(firstRoomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));
            reservation.setRoom(room);
        }

        // Guardar reserva inicial para obtener el ID
        reservationRepository.save(reservation);

        // Generar el reservationCode tipo "Reserva-123"
        String wompiReference = "Reserva-" + reservation.getReservationId();
        reservation.setReservationCode(wompiReference);
        reservationRepository.save(reservation);

        System.out.println("Referencia Wompi generada: " + wompiReference);

        //! WebSocket notificación en tiempo real
        webSocketNotificationService.notifyNewReservation(reservation);

        // Guardar habitaciones reservadas
        reservationRoomService.saveRoomsForReservation(reservation.getReservationId(), data.getRooms());

        List<ReservationRoomResponse> roomResponses = data.getRooms().stream().map(roomDTO -> {
            Room r = roomRepository.findById(roomDTO.getRoomId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));
            ReservationRoomResponse resp = new ReservationRoomResponse();
            resp.setRoomId(r.getRoomId());
            resp.setRoomName(r.getNameEs());
            resp.setAssignedRoomNumber(roomDTO.getAssignedRoomNumber());
            resp.setQuantity(roomDTO.getQuantity());
            return resp;
        }).collect(Collectors.toList());

        //? Generar HTML del correo
        String htmlBody = String.format("""
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <style>
    body {
      font-family: 'Segoe UI', 'Helvetica Neue', sans-serif;
      background-color: #f3f3f3;
      padding: 30px 15px;
      color: #333;
    }
    .container {
      background-color: #ffffff;
      padding: 40px;
      max-width: 700px;
      margin: auto;
      border-radius: 16px;
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.05);
    }
    .logo {
      text-align: center;
      margin-bottom: 25px;
    }
    .logo img {
      height: 70px;
    }
    h2 {
      color: #2E7D32;
      font-size: 1.8rem;
      text-align: center;
      margin-bottom: 30px;
    }
    .section-title {
      font-size: 1.1rem;
      color: #4E342E;
      margin-bottom: 10px;
      font-weight: bold;
    }
    .info-box {
      background-color: #FAFAFA;
      border: 1px solid #E0E0E0;
      padding: 20px;
      border-radius: 10px;
      font-size: 0.95rem;
      margin-bottom: 20px;
    }
    .info-box p {
      margin: 10px 0;
    }
    .highlight {
      color: #2E7D32;
      font-weight: 600;
    }
    .reservation-code {
      text-align: center;
      font-size: 1.2rem;
      color: #1B5E20;
      font-weight: bold;
      margin-top: 30px;
    }
    .footer {
      margin-top: 40px;
      text-align: center;
      font-size: 0.85rem;
      color: #777;
    }
    .contact-box {
      margin-top: 40px;
      font-size: 0.95rem;
      text-align: center;
      border-top: 1px solid #ddd;
      padding-top: 30px;
      color: #444;
    }
    .contact-box p {
      margin: 6px 0;
    }
    .contact-logo {
      font-size: 1.4rem;
      color: #2E7D32;
      font-weight: bold;
    }
    .icon {
      margin-right: 6px;
    }
    .social-icons {
      margin-top: 10px;
    }
    .social-icons a {
      margin: 0 6px;
      text-decoration: none;
      font-weight: bold;
      color: #555;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="logo">
      <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLo8t9NH1j1eo_tGo70lM2OcYKY4mhwhntvA&s" alt="Hotel Jardines de las Marías" />
    </div>
    <h2>¡Gracias por su reserva, %s!</h2>
    <div class="section-title">Resumen de la reserva</div>
    <div class="info-box">
      <p><span class="highlight">Fecha de entrada:</span> %s</p>
      <p><span class="highlight">Fecha de salida:</span> %s</p>
      <p><span class="highlight">Cantidad de personas:</span> %d</p>
      <p><span class="highlight">Cantidad de habitaciones:</span> %d</p>
    </div>
    <div class="reservation-code">Código de Reserva: %s</div>
    <div class="footer">
      Este es un mensaje automático. Si necesita asistencia, puede contactarnos:
    </div>
    <div class="contact-box">
      <div class="contact-logo">Hotel Jardines de las Marías</div>
      <p>📞 2562-8891</p>
      <p>📱 7890-5449</p>
      <p>✉️ jardindelasmariashotel@gmail.com</p>
      <p>📍 2 Avenida sur #23, Barrio el Calvario, Suchitoto</p>
      <div class="social-icons">
        <a href="https://www.facebook.com/hoteljardindelasmarias" target="_blank">Facebook</a> |
        <a href="https://www.instagram.com/hoteljardindelasmarias/" target="_blank">Instagram</a>
      </div>
    </div>
  </div>
</body>
</html>
""",
                reservation.getName(),
                reservation.getInitDate(),
                reservation.getFinishDate(),
                reservation.getCantPeople(),
                reservation.getQuantityReserved(),
                reservation.getReservationCode()
        );

        // Enviar correo
//        emailSenderService.sendMail(
//                reservation.getEmail(),
//                "Confirmación de Reserva - Hotel Jardines de las Marías",
//                htmlBody
//        );

        // Devolver respuesta incluyendo el enlace de pago
        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getReservationCode(),
                reservation.getInitDate(),
                reservation.getFinishDate(),
                reservation.getCantPeople(),
                reservation.getName(),
                reservation.getEmail(),
                reservation.getPhone(),
                reservation.getPayment(),
                reservation.getQuantityReserved(),
                reservation.getCreationDate(),
                reservation.getStatus(),
                roomResponses,
                reservation.getRoomNumber(),
                reservation.getDteControlNumber(),
                null

        );
    }



    @Override
    @Transactional
    public void update(ReservationDTO data, int reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));

        reservation.setInitDate(data.getInitDate());
        reservation.setFinishDate(data.getFinishDate());
        reservation.setCantPeople(data.getCantPeople());
        reservation.setName(data.getName());
        reservation.setEmail(data.getEmail());
        reservation.setPhone(data.getPhone());
        reservation.setPayment(data.getPayment());
        reservation.setRoomNumber(data.getRoomNumber());
        reservation.setStatus(data.getStatus());

        reservationRepository.save(reservation);

        reservationRoomService.deleteByReservationId(reservationId);
        reservationRoomService.saveRoomsForReservation(reservationId, data.getRooms());
    }

    @Override
    public void delete(int reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));
        reservationRepository.delete(reservation);
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> findById(int reservationId) {
        return reservationRepository.findById(reservationId);
    }

    @Override
    public List<Map<String, LocalDate>> getFullyBookedDatesForHotel() {
        List<Object[]> reservedDates = reservationRepository.findAllReservedDates();
        if (reservedDates.isEmpty()) return Collections.emptyList();

        long totalRooms = roomRepository.count();
        Map<LocalDate, Integer> reservationCountByDate = new HashMap<>();

        for (Object[] dates : reservedDates) {
            LocalDate startDate = (LocalDate) dates[0];
            LocalDate endDate = (LocalDate) dates[1];
            LocalDate current = startDate;
            while (!current.isAfter(endDate)) {
                reservationCountByDate.put(current, reservationCountByDate.getOrDefault(current, 0) + 1);
                current = current.plusDays(1);
            }
        }

        List<Map<String, LocalDate>> fullyBookedDates = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : reservationCountByDate.entrySet()) {
            if (entry.getValue() >= totalRooms) {
                Map<String, LocalDate> dateMap = new HashMap<>();
                dateMap.put("fullyBookedDate", entry.getKey());
                fullyBookedDates.add(dateMap);
            }
        }

        return fullyBookedDates;
    }

    @Override
    public List<ReservationResponse> getAllResponses() {
        return reservationRepository.findAll().stream()
                .map(res -> {
                    List<ReservationRoom> reservationRooms = reservationRoomRepository.findByReservation_ReservationId(res.getReservationId());

                    List<ReservationRoomResponse> roomResponses = reservationRooms.stream().map(rr -> {
                        Room r = rr.getRoom();
                        ReservationRoomResponse resp = new ReservationRoomResponse();
                        resp.setRoomId(r.getRoomId());
                        resp.setRoomName(r.getNameEs());
                        resp.setAssignedRoomNumber(rr.getAssignedRoomNumber());
                        resp.setQuantity(rr.getQuantity());
                        return resp;
                    }).collect(Collectors.toList());

                    return new ReservationResponse(
                            res.getReservationId(),
                            res.getReservationCode(),
                            res.getInitDate(),
                            res.getFinishDate(),
                            res.getCantPeople(),
                            res.getName(),
                            res.getEmail(),
                            res.getPhone(),
                            res.getPayment(),
                            res.getQuantityReserved(),
                            res.getCreationDate(),
                            res.getStatus(),
                            roomResponses,
                            res.getRoomNumber(),
                            res.getDteControlNumber(),
                            null
                    );
                }).collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> getAvailableRooms(LocalDate initDate, LocalDate finishDate, int cantPeople) {
        List<Room> allRooms = roomRepository.findAll();
        List<RoomResponse> availableRooms = new ArrayList<>();

        for (Room room : allRooms) {
            int reserved = reservationRoomRepository.countReservedQuantityForRoomInRange(
                    room.getRoomId(), initDate, finishDate
            );

            int disponibles = room.getQuantity() - reserved;

            CategoryRoomResponse categoryResponse = null;
            if (room.getCategoryRoom() != null) {
                var cat = room.getCategoryRoom();
                categoryResponse = new CategoryRoomResponse(
                        cat.getCategoryRoomId(),
                        cat.getNameCategoryEs(),
                        cat.getDescriptionEs(),
                        cat.getRoomSize(),
                        cat.getBedInfo(),
                        null,
                        Boolean.TRUE.equals(cat.getHasTv()),
                        Boolean.TRUE.equals(cat.getHasAc()),
                        Boolean.TRUE.equals(cat.getHasPrivateBathroom())
                );
            }

            availableRooms.add(new RoomResponse(
                    room.getRoomId(),
                    room.getNameEs(),
                    room.getNameEn(),
                    room.getMaxCapacity(),
                    room.getDescriptionEs(),
                    room.getDescriptionEn(),
                    room.getPrice(),
                    room.getSizeBed(),
                    room.getQuantity(),
                    room.getImg() != null ? room.getImg().getPath() : null,
                    disponibles,
                    categoryResponse,
                    disponibles > 0
            ));
        }

        return availableRooms;
    }

    @Override
    public void assignRoomNumber(int reservationId, String roomNumber) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));

        boolean roomInUse = reservationRepository.findActiveByRoomNumber(roomNumber).isPresent();
        if (roomInUse) {
            throw new CustomException(ErrorType.NOT_AVAILABLE, "Esa habitación ya está asignada a otra reserva activa.");
        }

        reservation.setRoomNumber(roomNumber);
        reservationRepository.save(reservation);
    }

    @Override
    public Reservation getActiveReservationByRoomNumber(String roomNumber) throws Exception {
        return reservationRepository.findActiveByRoomNumber(roomNumber)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reserva activa no encontrada."));
    }

    @Override
    public boolean isRoomNumberInUse(String roomNumber) {
        return reservationRepository.findActiveByRoomNumber(roomNumber).isPresent();
    }

    @Override
    public ReservationResponse getByRoomNumber(String roomNumber) throws Exception {
        Reservation reservation = reservationRepository.findTopByRoomNumber(roomNumber)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));

        List<ReservationRoom> reservationRooms = reservationRoomRepository.findByReservation_ReservationId(reservation.getReservationId());

        List<ReservationRoomResponse> roomResponses = reservationRooms.stream().map(rr -> {
            Room r = rr.getRoom();
            ReservationRoomResponse resp = new ReservationRoomResponse();
            resp.setRoomId(r.getRoomId());
            resp.setRoomName(r.getNameEs());
            resp.setAssignedRoomNumber(rr.getAssignedRoomNumber());
            resp.setQuantity(rr.getQuantity());
            return resp;
        }).collect(Collectors.toList());

        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getReservationCode(),
                reservation.getInitDate(),
                reservation.getFinishDate(),
                reservation.getCantPeople(),
                reservation.getName(),
                reservation.getEmail(),
                reservation.getPhone(),
                reservation.getPayment(),
                reservation.getQuantityReserved(),
                reservation.getCreationDate(),
                reservation.getStatus(),
                roomResponses,
                reservation.getRoomNumber(),
                reservation.getDteControlNumber(),
                null
        );
    }

    @Override
    public void assignRoomNumbers(int reservationId, List<ReservationRoomDTO> assignments) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));

        List<ReservationRoom> reservationRooms = reservationRoomRepository.findByReservation_ReservationId(reservationId);

        for (ReservationRoomDTO dto : assignments) {
            String assignedNumber = dto.getAssignedRoomNumber();

            if (assignedNumber != null && !assignedNumber.trim().isEmpty()) {
                boolean alreadyAssigned = reservationRoomRepository
                        .existsByAssignedRoomNumberAndOtherReservation(assignedNumber.trim(), reservationId);

                if (alreadyAssigned) {
                    throw new CustomException(ErrorType.NOT_AVAILABLE,
                            "La habitación número '" + assignedNumber + "' ya está asignada a otra reserva.");
                }
            }

            for (ReservationRoom rr : reservationRooms) {
                if (rr.getRoom().getRoomId() == dto.getRoomId()) {
                    rr.setAssignedRoomNumber(assignedNumber);
                    rr.setQuantity(dto.getQuantity());
                }
            }
        }

        reservationRoomRepository.saveAll(reservationRooms);
    }


    @Override
    public void saveEntity(Reservation reservation) {
        reservationRepository.save(reservation);
    }


    //?Buscar por medio de reserva
    @Override
    public ReservationResponse getByReservationCode(String reservationCode) throws Exception {
        Reservation reservation = reservationRepository.findByReservationCode(reservationCode)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Reservation"));

        List<ReservationRoom> reservationRooms =
                reservationRoomRepository.findByReservation_ReservationId(reservation.getReservationId());

        List<ReservationRoomResponse> roomResponses = reservationRooms.stream().map(rr -> {
            Room r = rr.getRoom();
            ReservationRoomResponse resp = new ReservationRoomResponse();
            resp.setRoomId(r.getRoomId());
            resp.setRoomName(r.getNameEs());
            resp.setAssignedRoomNumber(rr.getAssignedRoomNumber());
            resp.setQuantity(rr.getQuantity());
            return resp;
        }).collect(Collectors.toList());

        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getReservationCode(),
                reservation.getInitDate(),
                reservation.getFinishDate(),
                reservation.getCantPeople(),
                reservation.getName(),
                reservation.getEmail(),
                reservation.getPhone(),
                reservation.getPayment(),
                reservation.getQuantityReserved(),
                reservation.getCreationDate(),
                reservation.getStatus(),
                roomResponses,
                reservation.getRoomNumber(),
                reservation.getDteControlNumber(),
                null
        );
    }

    //!Metodo save con status

        @Override
    public ReservationResponse saveWithStatus(ReservationDTO data, String status) throws Exception {
        int totalReserved = data.getRooms().stream()
                .mapToInt(ReservationRoomDTO::getQuantity)
                .sum();

        Reservation reservation = new Reservation(
                data.getInitDate(),
                data.getFinishDate(),
                data.getCantPeople(),
                data.getName(),
                data.getEmail(),
                data.getPhone(),
                data.getPayment(),
                null,
                totalReserved
        );

        reservation.setStatus(status);
        reservation.setRoomNumber(data.getRoomNumber());

        if (!data.getRooms().isEmpty()) {
            int firstRoomId = data.getRooms().get(0).getRoomId();
            Room room = roomRepository.findById(firstRoomId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));
            reservation.setRoom(room);
        }

        // Guardar reserva inicial para obtener el ID
        reservationRepository.save(reservation);

        // Generar el reservationCode tipo "Reserva-123"
        String wompiReference = "Reserva-" + reservation.getReservationId();
        reservation.setReservationCode(wompiReference);
        reservationRepository.save(reservation);

        System.out.println("Referencia Wompi generada: " + wompiReference);

        webSocketNotificationService.notifyNewReservation(reservation);

        reservationRoomService.saveRoomsForReservation(reservation.getReservationId(), data.getRooms());

        List<ReservationRoomResponse> roomResponses = data.getRooms().stream().map(roomDTO -> {
            Room r = roomRepository.findById(roomDTO.getRoomId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Room"));
            ReservationRoomResponse resp = new ReservationRoomResponse();
            resp.setRoomId(r.getRoomId());
            resp.setRoomName(r.getNameEs());
            resp.setAssignedRoomNumber(roomDTO.getAssignedRoomNumber());
            resp.setQuantity(roomDTO.getQuantity());
            return resp;
        }).collect(Collectors.toList());

        //? Generar HTML del correo
        String htmlBody = String.format("""
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <style>
    body {
      font-family: 'Segoe UI', 'Helvetica Neue', sans-serif;
      background-color: #f3f3f3;
      padding: 30px 15px;
      color: #333;
    }
    .container {
      background-color: #ffffff;
      padding: 40px;
      max-width: 700px;
      margin: auto;
      border-radius: 16px;
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.05);
    }
    .logo {
      text-align: center;
      margin-bottom: 25px;
    }
    .logo img {
      height: 70px;
    }
    h2 {
      color: #2E7D32;
      font-size: 1.8rem;
      text-align: center;
      margin-bottom: 30px;
    }
    .section-title {
      font-size: 1.1rem;
      color: #4E342E;
      margin-bottom: 10px;
      font-weight: bold;
    }
    .info-box {
      background-color: #FAFAFA;
      border: 1px solid #E0E0E0;
      padding: 20px;
      border-radius: 10px;
      font-size: 0.95rem;
      margin-bottom: 20px;
    }
    .info-box p {
      margin: 10px 0;
    }
    .highlight {
      color: #2E7D32;
      font-weight: 600;
    }
    .reservation-code {
      text-align: center;
      font-size: 1.2rem;
      color: #1B5E20;
      font-weight: bold;
      margin-top: 30px;
    }
    .footer {
      margin-top: 40px;
      text-align: center;
      font-size: 0.85rem;
      color: #777;
    }
    .contact-box {
      margin-top: 40px;
      font-size: 0.95rem;
      text-align: center;
      border-top: 1px solid #ddd;
      padding-top: 30px;
      color: #444;
    }
    .contact-box p {
      margin: 6px 0;
    }
    .contact-logo {
      font-size: 1.4rem;
      color: #2E7D32;
      font-weight: bold;
    }
    .icon {
      margin-right: 6px;
    }
    .social-icons {
      margin-top: 10px;
    }
    .social-icons a {
      margin: 0 6px;
      text-decoration: none;
      font-weight: bold;
      color: #555;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="logo">
      <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLo8t9NH1j1eo_tGo70lM2OcYKY4mhwhntvA&s" alt="Hotel Jardines de las Marías" />
    </div>
    <h2>¡Gracias por su reserva, %s!</h2>
    <div class="section-title">Resumen de la reserva</div>
    <div class="info-box">
      <p><span class="highlight">Fecha de entrada:</span> %s</p>
      <p><span class="highlight">Fecha de salida:</span> %s</p>
      <p><span class="highlight">Cantidad de personas:</span> %d</p>
      <p><span class="highlight">Cantidad de habitaciones:</span> %d</p>
    </div>
    <div class="reservation-code">Código de Reserva: %s</div>
    <div class="footer">
      Este es un mensaje automático. Si necesita asistencia, puede contactarnos:
    </div>
    <div class="contact-box">
      <div class="contact-logo">Hotel Jardines de las Marías</div>
      <p>📞 2562-8891</p>
      <p>📱 7890-5449</p>
      <p>✉️ jardindelasmariashotel@gmail.com</p>
      <p>📍 2 Avenida sur #23, Barrio el Calvario, Suchitoto</p>
      <div class="social-icons">
        <a href="https://www.facebook.com/hoteljardindelasmarias" target="_blank">Facebook</a> |
        <a href="https://www.instagram.com/hoteljardindelasmarias/" target="_blank">Instagram</a>
      </div>
    </div>
  </div>
</body>
</html>
""",
                reservation.getName(),
                reservation.getInitDate(),
                reservation.getFinishDate(),
                reservation.getCantPeople(),
                reservation.getQuantityReserved(),
                reservation.getReservationCode()
        );

        // Enviar correo si lo necesitas:
        // emailSenderService.sendMail(
        //         reservation.getEmail(),
        //         "Confirmación de Reserva - Hotel Jardines de las Marías",
        //         htmlBody
        // );

        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getReservationCode(),
                reservation.getInitDate(),
                reservation.getFinishDate(),
                reservation.getCantPeople(),
                reservation.getName(),
                reservation.getEmail(),
                reservation.getPhone(),
                reservation.getPayment(),
                reservation.getQuantityReserved(),
                reservation.getCreationDate(),
                reservation.getStatus(),
                roomResponses,
                reservation.getRoomNumber(),
                reservation.getDteControlNumber(),
                null
        );
    }


    //! Enviar correos por medio de webhook

    @Override
    public String buildReservationEmailBody(Reservation reservation) {
        return String.format("""
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <style>
    body {
      font-family: 'Segoe UI', 'Helvetica Neue', sans-serif;
      background-color: #f3f3f3;
      padding: 30px 15px;
      color: #333;
    }
    .container {
      background-color: #ffffff;
      padding: 40px;
      max-width: 700px;
      margin: auto;
      border-radius: 16px;
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.05);
    }
    .logo {
      text-align: center;
      margin-bottom: 25px;
    }
    .logo img {
      height: 70px;
    }
    h2 {
      color: #2E7D32;
      font-size: 1.8rem;
      text-align: center;
      margin-bottom: 30px;
    }
    .section-title {
      font-size: 1.1rem;
      color: #4E342E;
      margin-bottom: 10px;
      font-weight: bold;
    }
    .info-box {
      background-color: #FAFAFA;
      border: 1px solid #E0E0E0;
      padding: 20px;
      border-radius: 10px;
      font-size: 0.95rem;
      margin-bottom: 20px;
    }
    .info-box p {
      margin: 10px 0;
    }
    .highlight {
      color: #2E7D32;
      font-weight: 600;
    }
    .reservation-code {
      text-align: center;
      font-size: 1.2rem;
      color: #1B5E20;
      font-weight: bold;
      margin-top: 30px;
    }
    .footer {
      margin-top: 40px;
      text-align: center;
      font-size: 0.85rem;
      color: #777;
    }
    .contact-box {
      margin-top: 40px;
      font-size: 0.95rem;
      text-align: center;
      border-top: 1px solid #ddd;
      padding-top: 30px;
      color: #444;
    }
    .contact-box p {
      margin: 6px 0;
    }
    .contact-logo {
      font-size: 1.4rem;
      color: #2E7D32;
      font-weight: bold;
    }
    .icon {
      margin-right: 6px;
    }
    .social-icons {
      margin-top: 10px;
    }
    .social-icons a {
      margin: 0 6px;
      text-decoration: none;
      font-weight: bold;
      color: #555;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="logo">
      <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLo8t9NH1j1eo_tGo70lM2OcYKY4mhwhntvA&s" alt="Hotel Jardines de las Marías" />
    </div>
    <h2>¡Gracias por su reserva, %s!</h2>
    <div class="section-title">Resumen de la reserva</div>
    <div class="info-box">
      <p><span class="highlight">Fecha de entrada:</span> %s</p>
      <p><span class="highlight">Fecha de salida:</span> %s</p>
      <p><span class="highlight">Cantidad de personas:</span> %d</p>
      <p><span class="highlight">Cantidad de habitaciones:</span> %d</p>
    </div>
    <div class="reservation-code">Código de Reserva: %s</div>
    <div class="footer">
      Este es un mensaje automático. Si necesita asistencia, puede contactarnos:
    </div>
    <div class="contact-box">
      <div class="contact-logo">Hotel Jardines de las Marías</div>
      <p>📞 2562-8891</p>
      <p>📱 7890-5449</p>
      <p>✉️ jardindelasmariashotel@gmail.com</p>
      <p>📍 2 Avenida sur #23, Barrio el Calvario, Suchitoto</p>
      <div class="social-icons">
        <a href="https://www.facebook.com/hoteljardindelasmarias" target="_blank">Facebook</a> |
        <a href="https://www.instagram.com/hoteljardindelasmarias/" target="_blank">Instagram</a>
      </div>
    </div>
  </div>
</body>
</html>
""",
                reservation.getName(),
                reservation.getInitDate(),
                reservation.getFinishDate(),
                reservation.getCantPeople(),
                reservation.getQuantityReserved(),
                reservation.getReservationCode()
        );
    }


}
