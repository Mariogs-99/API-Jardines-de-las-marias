package com.hotelJB.hotelJB_API.repositories;
import com.hotelJB.hotelJB_API.models.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByActiveTrueOrderByEventDateAsc();
}
