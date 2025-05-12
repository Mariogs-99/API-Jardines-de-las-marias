package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="category_room")
public class CategoryRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_room_id")
    private int categoryRoomId;

    @Column(name="name_category_es")
    private String nameCategoryEs;

    @Column(name="name_category_en")
    private String nameCategoryEn;

    @Column(name="description_es")
    private String descriptionEs;

    @Column(name="description_en")
    private String descriptionEn;

    public CategoryRoom(String nameCategoryEs, String nameCategoryEn, String descriptionEs, String descriptionEn) {
        this.nameCategoryEs = nameCategoryEs;
        this.descriptionEs = descriptionEs;
        this.nameCategoryEn = nameCategoryEn;
        this.descriptionEn = descriptionEn;
    }
}
