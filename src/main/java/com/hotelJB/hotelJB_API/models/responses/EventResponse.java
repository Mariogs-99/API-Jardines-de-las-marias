package com.hotelJB.hotelJB_API.models.responses;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {

    private Long eventId;

    private String title;
    private String description;

    private String titleEs;
    private String titleEn;

    private String descriptionEs;
    private String descriptionEn;

    private LocalDate eventDate;
    private Integer capacity;
    private BigDecimal price;

    private String imageUrl;

    private Boolean active;

    // Constructor personalizado opcional por idioma
    public EventResponse(com.hotelJB.hotelJB_API.models.entities.Event event, String lang) {
        this.eventId = event.getId();
        this.titleEs = event.getTitleEs();
        this.titleEn = event.getTitleEn();
        this.descriptionEs = event.getDescriptionEs();
        this.descriptionEn = event.getDescriptionEn();

        this.title = "en".equals(lang) ? event.getTitleEn() : event.getTitleEs();
        this.description = "en".equals(lang) ? event.getDescriptionEn() : event.getDescriptionEs();

        this.eventDate = event.getEventDate();
        this.capacity = event.getCapacity();
        this.price = event.getPrice();

        this.imageUrl = event.getImg() != null ? event.getImg().getPath() : null;

        this.active = event.isActive();
    }
}
