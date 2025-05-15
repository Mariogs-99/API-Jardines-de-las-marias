    package com.hotelJB.hotelJB_API.models.entities;

    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.util.List;

    @Data
    @Entity
    @NoArgsConstructor
    @Table(name="room")
    public class Room {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="room_id")
        private int roomId;

        @Column(name="name_es")
        private String nameEs;

        @Column(name="name_en")
        private String nameEn;

        @Column(name="max_capacity")
        private int maxCapacity;

        @Column(name="description_es")
        private String descriptionEs;

        @Column(name="description_en")
        private String descriptionEn;

        @Column(name="price")
        private double price;

        @Column(name="size_bed")
        private String sizeBed;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name="category_room_id")
        private CategoryRoom categoryRoom;

        @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name = "room_id", referencedColumnName = "room_id")
        private List<RoomxImg> roomImages;


        public Room(String nameEs, String nameEn, int maxCapacity, String descriptionEs, String descriptionEn,
                    double price, String sizeBed, CategoryRoom categoryRoom) {
            this.nameEs = nameEs;
            this.nameEn = nameEn;
            this.maxCapacity = maxCapacity;
            this.descriptionEs = descriptionEs;
            this.descriptionEn = descriptionEn;
            this.price = price;
            this.sizeBed = sizeBed;
            this.categoryRoom = categoryRoom;
        }
    }
