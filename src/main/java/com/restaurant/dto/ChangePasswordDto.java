package com.restaurant.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {

    private Long id;

    private String oldPassword;

    private String newPassword;

}
