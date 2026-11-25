package com.cognia.backend.dto;

import java.util.UUID;

public record AcceptInvitationResponse(
        UUID invitationId,
        String type,
        String status,
        StudentSummary student) {

}
