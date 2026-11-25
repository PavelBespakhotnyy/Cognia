package com.cognia.backend.dto;

import com.cognia.backend.entity.TutorInvitation;
import com.cognia.backend.util.TimeUtil;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TutorInvitationCreatedResponse(
        UUID id,
        UUID tutorId,
        String email,
        String status,
        OffsetDateTime expiresAt,
        String inviteUrl,
        OffsetDateTime createdAt) {

    public static TutorInvitationCreatedResponse from(TutorInvitation invitation, String inviteUrl) {
        return new TutorInvitationCreatedResponse(
                invitation.getId(),
                invitation.getTutor().getId(),
                invitation.getEmail(),
                invitation.getStatus(),
                TimeUtil.utc(invitation.getExpiresAt()),
                inviteUrl,
                TimeUtil.utc(invitation.getCreatedAt()));
    }
}
