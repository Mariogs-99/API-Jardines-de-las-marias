package com.hotelJB.hotelJB_API.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailRoomDTO {
    @NotBlank(message = "El nombre del detalle es obligatorio")
    @Size(max = 50, message = "El nombre del detalles no debe superar los 20 caracteres")
    private String detailNameEs;

    @NotBlank(message = "El nombre del detalle es obligatorio")
    @Size(max = 50, message = "El nombre del detalles no debe superar los 20 caracteres")
    private String detailNameEn;

    @NotBlank(message = "El nombre del icono del detalle es obligatorio")
    private String icon;
}
