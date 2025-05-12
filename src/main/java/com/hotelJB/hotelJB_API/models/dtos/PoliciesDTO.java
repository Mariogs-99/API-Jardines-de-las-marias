package com.hotelJB.hotelJB_API.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoliciesDTO {
    private String warrantyEs;
    private String warrantyEn;
    private String cancellationEs;
    private String cancellationEn;
}
