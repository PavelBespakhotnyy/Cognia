package com.cognia.backend.dto;

import com.cognia.backend.entity.Lesson;
import com.cognia.backend.util.TimeUtil;
import java.time.OffsetDateTime;
import java.util.UUID;

public record LessonResponse(
        UUID id,
        UUID tutorId,
        UUID studentId,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        String subject,
        String price,
        String status,
        String topic,
        String lessonNotes,
        boolean isAvailable,
        OffsetDateTime createdAt) {

    public static LessonResponse from(Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getTutor().getId(),
                lesson.getStudent() == null ? null : lesson.getStudent().getId(),
                TimeUtil.utc(lesson.getStartTime()),
                TimeUtil.utc(lesson.getEndTime()),
                lesson.getSubject(),
                lesson.getPrice().toPlainString(),
                lesson.getStatus(),
                lesson.getTopic(),
                lesson.getLessonNotes(),
                lesson.getStudent() == null,
                TimeUtil.utc(lesson.getCreatedAt()));
    }
}
