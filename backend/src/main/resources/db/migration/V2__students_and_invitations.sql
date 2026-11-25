CREATE TABLE students (
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL UNIQUE,
    tutor_id uuid,
    parent_id uuid,
    first_name varchar(100) NOT NULL,
    last_name varchar(100) NOT NULL,
    phone varchar(30),
    grade_level varchar(50),
    school_name varchar(150),
    notes text,
    created_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT students_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT students_tutor_id_fk FOREIGN KEY (tutor_id) REFERENCES tutors (id)
        ON DELETE RESTRICT ON UPDATE NO ACTION,
    CONSTRAINT students_parent_id_fk FOREIGN KEY (parent_id) REFERENCES parents (id)
        ON DELETE RESTRICT ON UPDATE NO ACTION
);

CREATE UNIQUE INDEX uq_students_id_tutor ON students (id, tutor_id);
CREATE UNIQUE INDEX uq_students_id_parent ON students (id, parent_id);

CREATE TABLE tutor_invitations (
    id uuid PRIMARY KEY,
    tutor_id uuid NOT NULL,
    email citext NOT NULL,
    token_hash varchar(255) NOT NULL UNIQUE,
    status varchar(10) NOT NULL DEFAULT 'PENDING',
    expires_at timestamptz NOT NULL,
    accepted_at timestamptz,
    accepted_by_student_id uuid,
    created_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT tutor_invitations_status_chk CHECK (status IN ('PENDING', 'ACCEPTED', 'EXPIRED', 'REVOKED')),
    CONSTRAINT tutor_invitations_tutor_id_fk FOREIGN KEY (tutor_id) REFERENCES tutors (id)
        ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT tutor_invitations_student_fk FOREIGN KEY (accepted_by_student_id) REFERENCES students (id)
        ON DELETE SET NULL ON UPDATE NO ACTION
);

CREATE INDEX idx_tutor_invitations_lookup ON tutor_invitations (tutor_id, email, status);

CREATE TABLE parent_invitations (
    id uuid PRIMARY KEY,
    parent_id uuid NOT NULL,
    email citext NOT NULL,
    token_hash varchar(255) NOT NULL UNIQUE,
    status varchar(10) NOT NULL DEFAULT 'PENDING',
    expires_at timestamptz NOT NULL,
    accepted_at timestamptz,
    accepted_by_student_id uuid,
    created_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT parent_invitations_status_chk CHECK (status IN ('PENDING', 'ACCEPTED', 'EXPIRED', 'REVOKED')),
    CONSTRAINT parent_invitations_parent_id_fk FOREIGN KEY (parent_id) REFERENCES parents (id)
        ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT parent_invitations_student_fk FOREIGN KEY (accepted_by_student_id) REFERENCES students (id)
        ON DELETE SET NULL ON UPDATE NO ACTION
);

CREATE INDEX idx_parent_invitations_lookup ON parent_invitations (parent_id, email, status);
