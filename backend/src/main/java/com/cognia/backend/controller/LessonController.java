package com.cognia.backend.controller;

import com.cognia.backend.api.ApiListResponse;
import com.cognia.backend.api.ApiResponse;
import com.cognia.backend.api.Pagination;
import com.cognia.backend.dto.CreateLessonRequest;
import com.cognia.backend.dto.LessonDetailResponse;
import com.cognia.backend.dto.LessonResponse;
import com.cognia.backend.security.JwtService;
import com.cognia.backend.security.RequestAuth;
import com.cognia.backend.service.LessonService;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final RequestAuth requestAuth;

    public LessonController(LessonService lessonService, RequestAuth requestAuth) {
        this.lessonService = lessonService;
        this.requestAuth = requestAuth;
    }

    @GetMapping
    public ApiListResponse<LessonResponse> list(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    OffsetDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    OffsetDateTime to,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        JwtService.JwtClaims claims = requestAuth.requireAnyRole(
                authorizationHeader, "TUTOR", "STUDENT", "PARENT");
        return Pagination.of(lessonService.list(claims, from, to), page, pageSize);
    }

    @PostMapping
    public ApiResponse<LessonResponse> create(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody CreateLessonRequest request) {
        JwtService.JwtClaims claims = requestAuth.requireRole(authorizationHeader, "TUTOR");
        return ApiResponse.ok(lessonService.create(claims.userId(), request));
    }

    @GetMapping("/{lessonId}")
    public ApiResponse<LessonDetailResponse> get(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable UUID lessonId) {
        JwtService.JwtClaims claims = requestAuth.requireAnyRole(
                authorizationHeader, "TUTOR", "STUDENT", "PARENT");
        return ApiResponse.ok(lessonService.get(claims, lessonId));
    }
}
