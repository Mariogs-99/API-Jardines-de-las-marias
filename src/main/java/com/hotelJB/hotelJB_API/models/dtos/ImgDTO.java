package com.hotelJB.hotelJB_API.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImgDTO {
    private String nameImg;
    private String path;
    private int categoryId;

    public ImgDTO(String nameImg, String path){
        this.nameImg = nameImg;
        this.path = path;
    }
}
