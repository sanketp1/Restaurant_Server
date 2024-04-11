package com.restaurant.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class CategoryDto {

    private Long id;

    private String name;

    private String description;

    private MultipartFile img;

    private byte[] returnedImg;

}
