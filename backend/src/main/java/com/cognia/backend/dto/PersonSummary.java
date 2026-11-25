package com.cognia.backend.dto;

import com.cognia.backend.entity.Parent;
import com.cognia.backend.entity.Student;
import com.cognia.backend.entity.Tutor;
import java.util.UUID;

public record PersonSummary(UUID id, String firstName, String lastName) {

    public static PersonSummary from(Tutor tutor) {
        return new PersonSummary(tutor.getId(), tutor.getFirstName(), tutor.getLastName());
    }

    public static PersonSummary from(Student student) {
        return new PersonSummary(student.getId(), student.getFirstName(), student.getLastName());
    }

    public static PersonSummary from(Parent parent) {
        return new PersonSummary(parent.getId(), parent.getFirstName(), parent.getLastName());
    }
}
