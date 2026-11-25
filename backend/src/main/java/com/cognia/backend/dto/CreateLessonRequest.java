package com.cognia.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateLessonRequest(
        UUID studentId,
        @NotNull OffsetDateTime startTime,
        @NotNull OffsetDateTime endTime,
        @Size(max = 100) String subject,
        @NotNull @DecimalMin(value = "0.00") BigDecimal price,
        @Size(max = 200) String topic,
        @Size(max = 2000) String lessonNotes) {

}
