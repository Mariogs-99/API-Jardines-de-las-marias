package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int roomId;

    @Column(name = "name_es")
    private String nameEs;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "max_capacity")
    private int maxCapacity;

    @Column(name = "description_es")
    private String descriptionEs;

    @Column(name = "description_en")
    private String descriptionEn;

    @Column(name = "price")
    private double price;

    @Column(name = "size_bed")
    private String sizeBed;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "available_quantity")
    private Integer availableQuantity; // <-- nuevo campo dinámico

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_room_id")
    private CategoryRoom categoryRoom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "img_id")
    private Img img;

    // Constructor para crear habitaciones desde código
    public Room(String nameEs, String nameEn, int maxCapacity, String descriptionEs, String descriptionEn,
                double price, String sizeBed, CategoryRoom categoryRoom, Integer quantity) {
        this.nameEs = nameEs;
        this.nameEn = nameEn;
        this.maxCapacity = maxCapacity;
        this.descriptionEs = descriptionEs;
        this.descriptionEn = descriptionEn;
        this.price = price;
        this.sizeBed = sizeBed;
        this.categoryRoom = categoryRoom;
        this.quantity = quantity;
        this.availableQuantity = quantity; // <-- inicializa como todas disponibles
    }

    public void setImg(Img img) {
        this.img = img;
    }
}
