package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private int categoryId;

    @Column(name="name_es")
    private String nameEs;

    @Column(name="name_en")
    private String nameEn;

    @Column(name="img")
    private String img;

    public Category(String nameEs,String nameEn, String img) {
        this.nameEs = nameEs;
        this.nameEn = nameEn;
        this.img = img;
    }
}
