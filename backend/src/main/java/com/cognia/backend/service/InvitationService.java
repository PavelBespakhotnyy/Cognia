package com.cognia.backend.service;

import com.cognia.backend.api.ApiException;
import com.cognia.backend.dto.AcceptInvitationRequest;
import com.cognia.backend.dto.AcceptInvitationResponse;
import com.cognia.backend.dto.CreateInvitationRequest;
import com.cognia.backend.dto.ParentInvitationCreatedResponse;
import com.cognia.backend.dto.ParentInvitationResponse;
import com.cognia.backend.dto.StudentSummary;
import com.cognia.backend.dto.TutorInvitationCreatedResponse;
import com.cognia.backend.dto.TutorInvitationResponse;
import com.cognia.backend.entity.Parent;
import com.cognia.backend.entity.ParentInvitation;
import com.cognia.backend.entity.Student;
import com.cognia.backend.entity.Tutor;
import com.cognia.backend.entity.TutorInvitation;
import com.cognia.backend.repository.ParentInvitationRepository;
import com.cognia.backend.repository.ParentRepository;
import com.cognia.backend.repository.StudentRepository;
import com.cognia.backend.repository.TutorInvitationRepository;
import com.cognia.backend.repository.TutorRepository;
import com.cognia.backend.security.InviteTokenUtil;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvitationService {

    private static final Duration INVITE_TTL = Duration.ofDays(7);

    private final TutorRepository tutorRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final TutorInvitationRepository tutorInvitationRepository;
    private final ParentInvitationRepository parentInvitationRepository;
    private final InviteTokenUtil tokenUtil;

    public InvitationService(
            TutorRepository tutorRepository,
            ParentRepository parentRepository,
            StudentRepository studentRepository,
            TutorInvitationRepository tutorInvitationRepository,
            ParentInvitationRepository parentInvitationRepository,
            InviteTokenUtil tokenUtil) {
        this.tutorRepository = tutorRepository;
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.tutorInvitationRepository = tutorInvitationRepository;
        this.parentInvitationRepository = parentInvitationRepository;
        this.tokenUtil = tokenUtil;
    }

    @Transactional
    public TutorInvitationCreatedResponse createTutorInvitation(
            UUID userId, CreateInvitationRequest request) {
        Tutor tutor = tutorRepository.findByUserId(userId)
                .orElseThrow(() -> notFound("Tutor profile was not found."));

        String token = tokenUtil.generate();
        TutorInvitation invitation = new TutorInvitation();
        invitation.setId(UUID.randomUUID());
        invitation.setTutor(tutor);
        invitation.setEmail(request.email().trim().toLowerCase(Locale.ROOT));
        invitation.setTokenHash(tokenUtil.hash(token));
        invitation.setStatus("PENDING");
        OffsetDateTime now = OffsetDateTime.now();
        invitation.setExpiresAt(now.plus(INVITE_TTL));
        invitation.setCreatedAt(now);
        tutorInvitationRepository.saveAndFlush(invitation);

        return TutorInvitationCreatedResponse.from(
                invitation, "https://cognia.dev/invite/tutor/" + token);
    }

    @Transactional(readOnly = true)
    public List<TutorInvitationResponse> listTutorInvitations(UUID userId, String status) {
        Tutor tutor = tutorRepository.findByUserId(userId)
                .orElseThrow(() -> notFound("Tutor profile was not found."));
        String normalizedStatus = normalizeStatus(status);
        return tutorInvitationRepository.findByTutorIdOrderByCreatedAtDesc(tutor.getId())
                .stream()
                .filter(invitation -> normalizedStatus == null || normalizedStatus.equals(invitation.getStatus()))
                .map(TutorInvitationResponse::from)
                .toList();
    }

    @Transactional
    public ParentInvitationCreatedResponse createParentInvitation(
            UUID userId, CreateInvitationRequest request) {
        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() -> notFound("Parent profile was not found."));

        String token = tokenUtil.generate();
        ParentInvitation invitation = new ParentInvitation();
        invitation.setId(UUID.randomUUID());
        invitation.setParent(parent);
        invitation.setEmail(request.email().trim().toLowerCase(Locale.ROOT));
        invitation.setTokenHash(tokenUtil.hash(token));
        invitation.setStatus("PENDING");
        OffsetDateTime now = OffsetDateTime.now();
        invitation.setExpiresAt(now.plus(INVITE_TTL));
        invitation.setCreatedAt(now);
        parentInvitationRepository.saveAndFlush(invitation);

        return ParentInvitationCreatedResponse.from(
                invitation, "https://cognia.dev/invite/parent/" + token);
    }

    @Transactional(readOnly = true)
    public List<ParentInvitationResponse> listParentInvitations(UUID userId, String status) {
        Parent parent = parentRepository.findByUserId(userId)
                .orElseThrow(() -> notFound("Parent profile was not found."));
        String normalizedStatus = normalizeStatus(status);
        return parentInvitationRepository.findByParentIdOrderByCreatedAtDesc(parent.getId())
                .stream()
                .filter(invitation -> normalizedStatus == null || normalizedStatus.equals(invitation.getStatus()))
                .map(ParentInvitationResponse::from)
                .toList();
    }

    @Transactional
    public AcceptInvitationResponse accept(UUID userId, AcceptInvitationRequest request) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.FORBIDDEN, "FORBIDDEN", "Only a student can accept an invitation."));

        String tokenHash = tokenUtil.hash(request.token().trim());

        TutorInvitation tutorInvitation = tutorInvitationRepository.findByTokenHash(tokenHash).orElse(null);
        if (tutorInvitation != null) {
            validateInvitation(tutorInvitation.getStatus(), tutorInvitation.getExpiresAt());
            if (student.getTutor() != null) {
                throw conflict("This student already has a tutor.");
            }
            tutorInvitation.setStatus("ACCEPTED");
            tutorInvitation.setAcceptedAt(OffsetDateTime.now());
            tutorInvitation.setAcceptedByStudent(student);
            tutorInvitationRepository.save(tutorInvitation);
            student.setTutor(tutorInvitation.getTutor());
            studentRepository.save(student);
            return new AcceptInvitationResponse(
                    tutorInvitation.getId(),
                    "TUTOR_STUDENT",
                    tutorInvitation.getStatus(),
                    StudentSummary.from(student));
        }

        ParentInvitation parentInvitation = parentInvitationRepository.findByTokenHash(tokenHash).orElse(null);
        if (parentInvitation != null) {
            validateInvitation(parentInvitation.getStatus(), parentInvitation.getExpiresAt());
            if (student.getParent() != null) {
                throw conflict("This student already has a parent.");
            }
            parentInvitation.setStatus("ACCEPTED");
            parentInvitation.setAcceptedAt(OffsetDateTime.now());
            parentInvitation.setAcceptedByStudent(student);
            parentInvitationRepository.save(parentInvitation);
            student.setParent(parentInvitation.getParent());
            studentRepository.save(student);
            return new AcceptInvitationResponse(
                    parentInvitation.getId(),
                    "PARENT_STUDENT",
                    parentInvitation.getStatus(),
                    StudentSummary.from(student));
        }

        throw notFound("Invitation was not found.");
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        String normalized = status.trim().toUpperCase(Locale.ROOT);
        if (!Set.of("PENDING", "ACCEPTED", "EXPIRED", "REVOKED").contains(normalized)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Unknown invitation status.");
        }
        return normalized;
    }

    private void validateInvitation(String status, OffsetDateTime expiresAt) {
        if (!"PENDING".equals(status)) {
            throw conflict("Invitation is no longer pending.");
        }
        if (expiresAt.isBefore(OffsetDateTime.now())) {
            throw conflict("Invitation has expired.");
        }
    }

    private ApiException notFound(String message) {
        return new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", message);
    }

    private ApiException conflict(String message) {
        return new ApiException(HttpStatus.CONFLICT, "CONFLICT", message);
    }
}
