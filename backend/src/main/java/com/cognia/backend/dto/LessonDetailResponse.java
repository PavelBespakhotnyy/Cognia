package com.cognia.backend.dto;

import com.cognia.backend.entity.Lesson;
import com.cognia.backend.util.TimeUtil;
import java.time.OffsetDateTime;
import java.util.UUID;

public record LessonDetailResponse(
        UUID id,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        String subject,
        String price,
        String status,
        String topic,
        String lessonNotes,
        boolean isAvailable,
        PersonSummary tutor,
        PersonSummary student,
        OffsetDateTime createdAt) {

    public static LessonDetailResponse from(Lesson lesson) {
        return new LessonDetailResponse(
                lesson.getId(),
                TimeUtil.utc(lesson.getStartTime()),
                TimeUtil.utc(lesson.getEndTime()),
                lesson.getSubject(),
                lesson.getPrice().toPlainString(),
                lesson.getStatus(),
                lesson.getTopic(),
                lesson.getLessonNotes(),
                lesson.getStudent() == null,
                PersonSummary.from(lesson.getTutor()),
                lesson.getStudent() == null ? null : PersonSummary.from(lesson.getStudent()),
                TimeUtil.utc(lesson.getCreatedAt()));
    }
}
