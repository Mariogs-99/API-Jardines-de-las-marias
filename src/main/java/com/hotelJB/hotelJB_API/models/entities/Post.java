package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private int postId;

    @Column(name="title_es")
    private String titleEs;

    @Column(name="title_en")
    private String titleEn;

    @Column(name="description_es")
    private String descriptionEs;

    @Column(name="description_en")
    private String descriptionEn;

    @Column(name="path_image")
    private String pathImage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id")
    private Category category;

    public Post(String titleEs, String titleEn, String descriptionEs, String descriptionEn,
                String pathImage, Category category) {
        this.titleEs = titleEs;
        this.titleEn = titleEn;
        this.descriptionEs = descriptionEs;
        this.descriptionEn = descriptionEn;
        this.pathImage = pathImage;
        this.category = category;
    }
}
