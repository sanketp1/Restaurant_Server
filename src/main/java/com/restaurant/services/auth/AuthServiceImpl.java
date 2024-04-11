package com.restaurant.services.auth;

import com.restaurant.dto.ChangePasswordDto;
import com.restaurant.dto.SignupRequest;
import com.restaurant.dto.UserDto;
import com.restaurant.entity.User;
import com.restaurant.enums.UserRole;
import com.restaurant.repo.UserRepo;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void createAdminAccount() {
        User adminAccount = userRepo.findByUserRole(UserRole.ADMIN);
        if (null == adminAccount) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setUserRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepo.save(user);
        }
    }

    @Transactional
    public UserDto createUser(SignupRequest signupRequest) throws Exception {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.CUSTOMER);
        User createdUser = userRepo.save(user);
        UserDto createdUserDto = new UserDto();
        createdUserDto.setId(createdUser.getId());
        return createdUserDto;
    }


    public Boolean hasUserWithEmail(String email) {
        return userRepo.findFirstByEmail(email).isPresent();
    }

    @Override
    public UserDto getUserById(Long userId) {
        UserDto userDto = new UserDto();
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            userDto = optionalUser.get().getUserDto();
            userDto.setReturnedImg(optionalUser.get().getImg());
        }
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) throws IOException {
        Optional<User> userOptional = userRepo.findById(userDto.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(userDto.getEmail());
            user.setName(userDto.getName());
            user.setImg(userDto.getImg().getBytes());
            User updatedUser = userRepo.save(user);
            UserDto updatedUserDto = new UserDto();
            updatedUserDto.setId(updatedUser.getId());
            return updatedUserDto;
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<?> updatePasswordById(ChangePasswordDto changePasswordDto) {
        User user = null;
        try {
            Optional<User> userOptional = userRepo.findById(changePasswordDto.getId());
            if (userOptional.isPresent()) {
                user = userOptional.get();
                if (this.passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
                    User updateUser = userRepo.save(user);
                    UserDto userDto = new UserDto();
                    userDto.setId(updateUser.getId());
                    return ResponseEntity.status(HttpStatus.OK).body(userDto);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Old password is incorrect");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

}
