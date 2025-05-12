package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="gallery")
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="gallery_id")
    private int galleryId;

    @Column(name="name_img")
    private String nameImg;

    @Column(name="path")
    private String path;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id")
    private Category category;

    public Gallery(String nameImg, String path, Category category) {
        this.nameImg = nameImg;
        this.path = path;
        this.category = category;
    }
}
