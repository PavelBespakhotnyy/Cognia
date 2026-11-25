package com.cognia.backend.controller;

import com.cognia.backend.api.ApiListResponse;
import com.cognia.backend.api.ApiResponse;
import com.cognia.backend.api.Pagination;
import com.cognia.backend.dto.AcceptInvitationRequest;
import com.cognia.backend.dto.AcceptInvitationResponse;
import com.cognia.backend.dto.CreateInvitationRequest;
import com.cognia.backend.dto.ParentInvitationCreatedResponse;
import com.cognia.backend.dto.ParentInvitationResponse;
import com.cognia.backend.dto.TutorInvitationCreatedResponse;
import com.cognia.backend.dto.TutorInvitationResponse;
import com.cognia.backend.security.JwtService;
import com.cognia.backend.security.RequestAuth;
import com.cognia.backend.service.InvitationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;
    private final RequestAuth requestAuth;

    public InvitationController(InvitationService invitationService, RequestAuth requestAuth) {
        this.invitationService = invitationService;
        this.requestAuth = requestAuth;
    }

    @PostMapping("/tutor")
    public ApiResponse<TutorInvitationCreatedResponse> createTutorInvitation(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody CreateInvitationRequest request) {
        JwtService.JwtClaims claims = requestAuth.requireRole(authorizationHeader, "TUTOR");
        return ApiResponse.ok(invitationService.createTutorInvitation(claims.userId(), request));
    }

    @GetMapping("/tutor")
    public ApiListResponse<TutorInvitationResponse> listTutorInvitations(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String status) {
        JwtService.JwtClaims claims = requestAuth.requireRole(authorizationHeader, "TUTOR");
        return Pagination.of(
                invitationService.listTutorInvitations(claims.userId(), status), page, pageSize);
    }

    @PostMapping("/parent")
    public ApiResponse<ParentInvitationCreatedResponse> createParentInvitation(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody CreateInvitationRequest request) {
        JwtService.JwtClaims claims = requestAuth.requireRole(authorizationHeader, "PARENT");
        return ApiResponse.ok(invitationService.createParentInvitation(claims.userId(), request));
    }

    @GetMapping("/parent")
    public ApiListResponse<ParentInvitationResponse> listParentInvitations(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String status) {
        JwtService.JwtClaims claims = requestAuth.requireRole(authorizationHeader, "PARENT");
        return Pagination.of(
                invitationService.listParentInvitations(claims.userId(), status), page, pageSize);
    }

    @PostMapping("/accept")
    public ApiResponse<AcceptInvitationResponse> accept(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody AcceptInvitationRequest request) {
        JwtService.JwtClaims claims = requestAuth.requireRole(authorizationHeader, "STUDENT");
        return ApiResponse.ok(invitationService.accept(claims.userId(), request));
    }
}
