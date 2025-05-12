package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="sections")
public class Sections {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="section_id")
    private int sectionId;

    @Column(name="title_es")
    private String titleEs;

    @Column(name="title_en")
    private String titleEn;

    @Column(name="description_es")
    private String descriptionEs;

    @Column(name="description_en")
    private String descriptionEn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id")
    private Category category;

    public Sections(String titleEs, String titleEn, String descriptionEs, String descriptionEn, Category category) {
        this.titleEs = titleEs;
        this.titleEn = titleEn;
        this.descriptionEs = descriptionEs;
        this.descriptionEn = descriptionEn;
        this.category = category;
    }
}
