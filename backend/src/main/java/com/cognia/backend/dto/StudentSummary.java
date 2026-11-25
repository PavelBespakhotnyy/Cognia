package com.cognia.backend.dto;

import com.cognia.backend.entity.Student;
import java.util.UUID;

public record StudentSummary(
        UUID id,
        String firstName,
        String lastName,
        UUID tutorId,
        UUID parentId) {

    public static StudentSummary from(Student student) {
        return new StudentSummary(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getTutor() == null ? null : student.getTutor().getId(),
                student.getParent() == null ? null : student.getParent().getId());
    }
}
