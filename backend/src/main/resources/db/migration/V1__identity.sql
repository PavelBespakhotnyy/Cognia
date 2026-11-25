CREATE EXTENSION IF NOT EXISTS citext;

CREATE TABLE users (
    id uuid PRIMARY KEY,
    email citext NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    role varchar(10) NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT users_role_chk CHECK (role IN ('TUTOR', 'STUDENT', 'PARENT'))
);

CREATE TABLE tutors (
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL UNIQUE,
    first_name varchar(100) NOT NULL,
    last_name varchar(100) NOT NULL,
    phone varchar(30),
    bio text,
    created_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT tutors_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE parents (
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL UNIQUE,
    first_name varchar(100) NOT NULL,
    last_name varchar(100) NOT NULL,
    phone varchar(30),
    preferred_payment_method varchar(30),
    created_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT parents_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE documents (
    id uuid PRIMARY KEY,
    owner_id uuid NOT NULL,
    file_name varchar(255) NOT NULL,
    file_url varchar(500) NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT documents_owner_id_fk FOREIGN KEY (owner_id) REFERENCES tutors (id)
        ON DELETE CASCADE ON UPDATE NO ACTION
);

COMMENT ON TABLE users IS 'Base authentication table. Email uniqueness is case-insensitive.';
COMMENT ON TABLE documents IS 'Tutor file library. Created in V1 because lesson_materials will reference it.';
