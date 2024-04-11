package com.restaurant.dto;

import com.restaurant.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String jwt;

    private UserRole userRole;

    private Long userId;

}
