package com.restaurant.controller;


import com.restaurant.dto.*;
import com.restaurant.entity.User;
import com.restaurant.repo.UserRepo;
import com.restaurant.services.auth.AuthService;
import com.restaurant.services.jwt.UserService;
import com.restaurant.utill.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final UserRepo userRepo;

    private final JwtUtil jwtUtil;

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody(required = true) SignupRequest signupRequest) throws Exception {

        if (authService.hasUserWithEmail(signupRequest.getEmail()))
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);

        UserDto createdUser = authService.createUser(signupRequest);
        if (createdUser == null)
            return new ResponseEntity<>("User not created, come again later", HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws BadCredentialsException, DisabledException, UsernameNotFoundException, IOException, JSONException, ServletException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password.");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User is not activated");
            return null;
        }
        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = userRepo.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;
    }

    @PostMapping("/update")
    public ResponseEntity<UserDto> updateProfile(@ModelAttribute UserDto userDto) throws IOException {
        UserDto updatedUser = authService.updateUser(userDto);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        UserDto userDto = authService.getUserById(userId);
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        try {
            return authService.updatePasswordById(changePasswordDto);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
    }

}
