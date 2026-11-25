package com.cognia.backend.dto;

import com.cognia.backend.entity.Student;
import com.cognia.backend.util.TimeUtil;
import java.time.OffsetDateTime;
import java.util.UUID;

public record StudentResponse(
        UUID id,
        UUID userId,
        UUID tutorId,
        UUID parentId,
        String firstName,
        String lastName,
        String phone,
        String gradeLevel,
        String schoolName,
        String notes,
        OffsetDateTime createdAt) {

    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getUser().getId(),
                student.getTutor() == null ? null : student.getTutor().getId(),
                student.getParent() == null ? null : student.getParent().getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getPhone(),
                student.getGradeLevel(),
                student.getSchoolName(),
                student.getNotes(),
                TimeUtil.utc(student.getCreatedAt()));
    }
}
