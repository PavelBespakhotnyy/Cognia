# Cognia Database Schema

This folder contains the database design for the Cognia platform, designed for PostgreSQL 16 and
managed with Flyway migrations in `backend/src/main/resources/db/migration/`.

The current design source is `schema_v5.1.dbml`. Version 5.0 is kept for history only. Do not edit
either version in place. Create `schema_v5.2.dbml` for the next schema change.

## Contents

| File | Purpose |
|------|---------|
| `schema_v5.0.dbml` | Previous design, kept for history |
| `schema_v5.1.dbml` | Current source schema, valid DBML for dbdiagram.io |
| `README.md` | This file: overview, conventions, and migration order |

`schema_v5.1.png` should be exported from dbdiagram.io when the diagram is reviewed.

## Domain overview

Cognia is a tutoring management platform with three user roles:

| Role | Platform | Description |
|------|----------|-------------|
| `TUTOR` | Web (React) | Private teacher. Self-registers on the web. |
| `STUDENT` | Mobile (Android) | Pupil. Self-registers in the Android app. |
| `PARENT` | Mobile (Android) | Parent or guardian. Self-registers in the Android app. |

The web application is tutor-only. Students and parents use one Android app, and the backend
determines the role from the account. Tutors do not create student or parent accounts.

Self-registration is now modeled. A tutor or a parent sends an email invitation. The invite does
not create an account. The student registers independently and accepts the token. Only then are
`students.tutor_id` and `students.parent_id` set.

## Table groups

There are 23 tables in v5.1.

### 1. Identity and invitations

| Table | Purpose |
|-------|---------|
| `users` | Base authentication table with email, password hash, role, and creation time |
| `tutors` | Tutor profile, one row per `TUTOR` account |
| `parents` | Parent profile, one row per `PARENT` account |
| `students` | Student profile, linked to a tutor and a parent only after invitation acceptance |
| `tutor_invitations` | Email invitation from a tutor to a student |
| `parent_invitations` | Email invitation from a parent to a student |

Key constraints:

- `users.role` is limited to `TUTOR`, `STUDENT`, and `PARENT`.
- `users.email` uses `citext`, so uniqueness is case-insensitive.
- Profile `user_id` values are unique and reference `users.id`.
- Invitation `token_hash` values are unique.

### 2. Lessons and files

| Table | Purpose |
|-------|---------|
| `documents` | Tutor file library, created in V1 |
| `lessons` | Lesson calendar with time, subject, price, status, topic, and notes |
| `lesson_materials` | Attachments linking a lesson to one document |

Key constraints:

- `end_time > start_time`.
- One document can be attached to a lesson only once.
- A tutor cannot have two lessons with the same `start_time`.
- `is_available` is not stored. A free slot is a lesson with `student_id is null`.

### 3. Student indicators

| Table | Purpose |
|-------|---------|
| `attendance` | One attendance value per student per lesson |
| `assignments` | Homework for one student in one lesson |
| `grades` | One grade per assignment |
| `exams` | Exam results recorded by a tutor |

Key constraints:

- `attendance(lesson_id, student_id)` is unique.
- `assignments(lesson_id, student_id, title)` is unique.
- `grades.assignment_id` is unique.
- `score <= max_score` for grades and exams.

### 4. Finance and statistics

| Table | Purpose |
|-------|---------|
| `payments` | At most one payment per lesson |
| `statistics` | Prepared monthly numbers for the tutor dashboard |

Key constraints:

- `payments.lesson_id` is unique.
- `payments(student_id)` must match the student of the referenced lesson through a composite FK.
- `statistics(tutor_id, month_year)` is unique.
- `month_year` is a `date` fixed to the first day of the month.
- `total_expenses` was removed because no expense source table exists. Recalculate statistics on a
  schedule or expose them through a view.

### 5. Communication

| Table | Purpose |
|-------|---------|
| `reports` | Report text sent to a parent after a lesson |
| `messages` | Internal chat between two users |

Key constraints:

- `reports(lesson_id, parent_id)` is unique.
- Composite FKs keep the report student consistent with the lesson and with the parent.
- `messages.sender_id` must be different from `messages.receiver_id`.

### 6. Gamification

| Table | Purpose |
|-------|---------|
| `student_points` | One point total per student |
| `badges` | Badge catalog |
| `student_badges` | Award history, one award of a badge per student |

The old `rewards` table was split. Storing points and badge rows in one table duplicated the point
total for students with more than one badge.

### 7. Roadmap

| Table | Purpose |
|-------|---------|
| `invoices` | Future PDF invoices for parents |
| `goals` | Future academic goals |
| `notifications` | Future push and email history |

The invalid relation between `notifications.user_id` and `notifications.type` was removed. `type`
is a checked varchar value, not a foreign key.

## Conventions

- Primary keys are `uuid` values generated by the application. The database does not generate them.
- Timestamps use `timestamptz`. Dates such as `month_year`, invoice dates, and goal targets use
  `date`.
- Email fields use `citext`. V1 must include `CREATE EXTENSION IF NOT EXISTS citext;`.
- Money uses `decimal(10,2)`. Scores use `decimal(4,2)`. Never use float.
- Status values are enforced by DBML `check` expressions. These must be carried into the SQL
  migrations.
- Composite unique rules are written as `indexes` blocks with `[unique]`.
- Foreign key actions are written on `Ref:` lines with `delete:` and `update:` settings. History
  tables mostly use `restrict`. Child rows of a deleted parent use `cascade` only when the child has
  no independent history.
- Never create or change tables by hand in psql, DBeaver, or pgAdmin. Add a new migration file.
- Never edit a migration that Flyway already applied. Flyway stores checksums and will fail.

## How to work with the schema

### Edit the schema

1. Copy `schema_v5.1.dbml` to `schema_v5.2.dbml`.
2. Edit the new file with dbdiagram.io or any DBML-compatible editor.
3. Export the updated diagram as `schema_v5.2.png`.
4. Keep all older versions. They document the evolution of the schema.

### Validate the DBML

The v5.1 file was validated with the DBML CLI:

```bash
npx --yes -p @dbml/cli dbml2sql --postgres docs/database/schema_v5.1.dbml -o /tmp/schema_v5.1.sql
```

A successful command prints `Generated SQL dump file`. The generated SQL was also executed in a
temporary PostgreSQL database with `citext` installed. The live `cognia` database was not touched.

### Generate a diagram

1. Go to dbdiagram.io.
2. Paste the contents of `docs/database/schema_v5.1.dbml`.
3. Click Export, then PNG.
4. Save as `docs/database/schema_v5.1.png`.

### Turn the schema into Flyway migrations

DBML is a design document and is not executed directly. Real SQL migrations live in
`backend/src/main/resources/db/migration/`.

| Migration | Tables | Status |
|-----------|--------|--------|
| V1 | `users`, `tutors`, `parents`, `documents`, plus the `citext` extension | Applied |
| V2 | `students`, `tutor_invitations`, `parent_invitations` | Applied |
| V3 | `lessons` | Applied |
| V4 | `lesson_materials` | Planned |
| V5 | `attendance` | Planned |
| V6 | `assignments`, `grades` | Planned |
| V7 | `exams` | Planned |
| V8 | `payments`, `statistics` | Planned |
| V9 | `reports`, `student_points`, `badges`, `student_badges` | Planned |
| V10 | `messages` | Planned |
| V11 | `invoices`, `goals`, `notifications` | Planned |

Each `V*.sql` file is written by hand or generated from DBML and then reviewed. Flyway applies
them automatically when the backend starts.

Current status on 2026-11-24: V1, V2 and V3 are applied in the local `cognia` database. V4 through
V11 are still planned. See `info.md` for verified details and `info_tech.md` for plain explanations.

## Related documents

- `instructions.md`: local run commands and what each command does
- `info.md`: current verified state and decisions
- `info_tech.md`: plain-language explanations
- `backend/src/main/resources/db/migration/`: actual SQL migrations, V1 through V3 applied locally
- `infra/docker-compose.yml`: local PostgreSQL instance
