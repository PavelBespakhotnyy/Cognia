package com.cognia.backend.controller;

import com.cognia.backend.api.ApiListResponse;
import com.cognia.backend.api.Pagination;
import com.cognia.backend.dto.StudentResponse;
import com.cognia.backend.security.JwtService;
import com.cognia.backend.security.RequestAuth;
import com.cognia.backend.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final RequestAuth requestAuth;

    public StudentController(StudentService studentService, RequestAuth requestAuth) {
        this.studentService = studentService;
        this.requestAuth = requestAuth;
    }

    @GetMapping
    public ApiListResponse<StudentResponse> list(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        JwtService.JwtClaims claims = requestAuth.requireRole(authorizationHeader, "TUTOR");
        return Pagination.of(studentService.listForTutor(claims.userId()), page, pageSize);
    }
}
