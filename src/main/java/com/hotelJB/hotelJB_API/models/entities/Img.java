package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "img")
public class Img {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private int imgId;

    @Column(name = "name_img")
    private String nameImg;

    @Column(name = "path")
    private String path;

    public Img(String nameImg, String path) {
        this.nameImg = nameImg;
        this.path = path;
    }

}


