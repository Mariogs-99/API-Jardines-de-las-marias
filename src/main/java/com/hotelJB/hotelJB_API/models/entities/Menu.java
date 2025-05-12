package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="menu_id")
    private int menuId;

    @Column(name="title")
    private String title;

    @Column(name="path_pdf")
    private String pathPdf;

    @Column(name="schedule")
    private String schedule;


    public Menu(String title, String pathPdf, String schedule) {
        this.title = title;
        this.pathPdf = pathPdf;
        this.schedule = schedule;
    }
}
