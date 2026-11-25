package com.cognia.backend.controller;

import com.cognia.backend.api.ApiResponse;
import com.cognia.backend.dto.AuthResponseData;
import com.cognia.backend.dto.LoginRequest;
import com.cognia.backend.dto.RegisterParentRequest;
import com.cognia.backend.dto.RegisterStudentRequest;
import com.cognia.backend.dto.RegisterTutorRequest;
import com.cognia.backend.dto.UserResponse;
import com.cognia.backend.security.JwtService;
import com.cognia.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register/tutor")
    public ApiResponse<UserResponse> registerTutor(
            @Valid @RequestBody RegisterTutorRequest request) {
        return ApiResponse.ok(authService.registerTutor(request));
    }

    @PostMapping("/register/student")
    public ApiResponse<UserResponse> registerStudent(
            @Valid @RequestBody RegisterStudentRequest request) {
        return ApiResponse.ok(authService.registerStudent(request));
    }

    @PostMapping("/register/parent")
    public ApiResponse<UserResponse> registerParent(
            @Valid @RequestBody RegisterParentRequest request) {
        return ApiResponse.ok(authService.registerParent(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponseData> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        JwtService.JwtClaims claims = jwtService.parseBearer(authorizationHeader);
        return ApiResponse.ok(authService.currentUser(claims.userId()));
    }
}
