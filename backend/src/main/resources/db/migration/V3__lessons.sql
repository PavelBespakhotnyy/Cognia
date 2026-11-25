CREATE TABLE lessons (
    id uuid PRIMARY KEY,
    tutor_id uuid NOT NULL,
    student_id uuid,
    start_time timestamptz NOT NULL,
    end_time timestamptz NOT NULL,
    subject varchar(100),
    price decimal(10,2) NOT NULL,
    status varchar(12) NOT NULL DEFAULT 'SCHEDULED',
    topic varchar(200),
    lesson_notes text,
    created_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT lessons_price_chk CHECK (price >= 0),
    CONSTRAINT lessons_status_chk CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT lessons_time_order_chk CHECK (end_time > start_time),
    CONSTRAINT lessons_tutor_id_fk FOREIGN KEY (tutor_id) REFERENCES tutors (id)
        ON DELETE RESTRICT ON UPDATE NO ACTION,
    CONSTRAINT lessons_student_id_fk FOREIGN KEY (student_id) REFERENCES students (id)
        ON DELETE RESTRICT ON UPDATE NO ACTION
);

CREATE UNIQUE INDEX uq_lessons_id_student ON lessons (id, student_id);
CREATE UNIQUE INDEX uq_lessons_tutor_start ON lessons (tutor_id, start_time);

COMMENT ON TABLE lessons IS 'Lesson calendar. A free slot has student_id = null.';
