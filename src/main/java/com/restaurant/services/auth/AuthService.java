package com.restaurant.services.auth;


import com.restaurant.dto.ChangePasswordDto;
import com.restaurant.dto.SignupRequest;
import com.restaurant.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AuthService {

     UserDto createUser(SignupRequest signupRequest) throws Exception;

     Boolean hasUserWithEmail(String email);

     UserDto getUserById(Long userId);

     UserDto updateUser(UserDto userDto) throws IOException;

     ResponseEntity<?> updatePasswordById(ChangePasswordDto changePasswordDto);

}
