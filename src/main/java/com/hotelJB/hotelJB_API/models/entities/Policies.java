package com.hotelJB.hotelJB_API.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="policies")
public class Policies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="policies_id")
    private int policiesId;

    @Column(name="warranty_es")
    private String warrantyEs;

    @Column(name="warranty_en")
    private String warrantyEn;

    @Column(name="cancellation_es")
    private String cancellationEs;

    @Column(name="cancellation_en")
    private String cancellationEn;

    public Policies(String warrantyEs, String warrantyEn ,String cancellationEs, String cancellationEn) {
        this.warrantyEs = warrantyEs;
        this.warrantyEn = warrantyEn;
        this.cancellationEs = cancellationEs;
        this.cancellationEn = cancellationEn;
    }
}
