package com.cognia.backend.dto;

import com.cognia.backend.entity.TutorInvitation;
import com.cognia.backend.util.TimeUtil;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TutorInvitationResponse(
        UUID id,
        UUID tutorId,
        String email,
        String status,
        OffsetDateTime expiresAt,
        OffsetDateTime acceptedAt,
        UUID acceptedByStudentId,
        OffsetDateTime createdAt) {

    public static TutorInvitationResponse from(TutorInvitation invitation) {
        return new TutorInvitationResponse(
                invitation.getId(),
                invitation.getTutor().getId(),
                invitation.getEmail(),
                invitation.getStatus(),
                TimeUtil.utc(invitation.getExpiresAt()),
                TimeUtil.utc(invitation.getAcceptedAt()),
                invitation.getAcceptedByStudent() == null ? null : invitation.getAcceptedByStudent().getId(),
                TimeUtil.utc(invitation.getCreatedAt()));
    }
}
