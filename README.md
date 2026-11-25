# Cognia

This is a platform for students, their parents and private tutors to organize study material.

## Team
Our team is very small, just 2 people!

[Pavel Bespakhotnyi](https://github.com/PavelBespakhotnyy) and [Alla Kolchanova](https://github.com/Kolchan1477)

### Goal
This project is aimed to be a learning oportunity and not a commercial product.

## Current implementation status (2026-11-24)

The backend is being built in small vertical slices. Frontend work is out of scope for the current steps.

- Database: Flyway migrations V1 through V3 are applied in the local `cognia` database.
  8 of 23 designed tables exist.
- Backend: auth, invitations, student list, and lesson endpoints are implemented.
  14 of 23 endpoints in `api-contract-preview.json` are marked `IMPLEMENTED`.
- Verification: manual curl and PostgreSQL checks pass. There is no automated test suite yet.
- Local run commands and explanations: `instructions.md`.
- Source of truth: `info.md` keeps the verified state and decisions.
  `info_tech.md` explains the same parts in plain language.

Next slice: `V4__lesson_materials.sql` with document and lesson material endpoints.

cognia/
│
├── docs/                              # Documents
│   ├── database/
│   │   └── schema_v5.0.dbml
│   ├── latex/
│   │   └── memoria.tex
│   ├── figma_exports/
│   └── api/
│       └── openapi.yaml               # OpenAPI-spec (single contract for web + android)
│
├── backend/                           # Spring Boot (Java)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/cognia/
│   │   │   │   ├── config/            # Security, CORS, JWT
│   │   │   │   ├── auth/              # Login, registration, JWT
│   │   │   │   ├── users/             # users (base)
│   │   │   │   ├── tutors/            # tutors
│   │   │   │   ├── students/          # students
│   │   │   │   ├── parents/           # parents
│   │   │   │   ├── lessons/           # lessons + lesson_materials
│   │   │   │   ├── attendance/        # attendance
│   │   │   │   ├── assignments/       # assignments + grades
│   │   │   │   ├── exams/             # exams
│   │   │   │   ├── payments/          # payments + statistics
│   │   │   │   ├── messages/          # messages
│   │   │   │   ├── documents/         # documents
│   │   │   │   ├── reports/           # reports
│   │   │   │   └── rewards/           # rewards
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-dev.yml
│   │   │       └── db/migration/      # Flyway: V1__init.sql, V2__...
│   │   └── test/java/com/cognia/
│   ├── build.gradle
│   ├── gradlew
│   ├── gradlew.bat
│   ├── settings.gradle
│   └── Dockerfile
│
├── frontend-web/                      # Vite + React + TS (tutor space)
│   ├── .storybook/
│   │   ├── main.ts
│   │   └── preview.ts
│   ├── public/
│   ├── src/
│   │   ├── components/                # UI components (without .stories!)
│   │   ├── pages/                     # Screens
│   │   ├── stories/                   # All Storybook Stories
│   │   ├── hooks/
│   │   ├── utils/
│   │   ├── types/
│   │   ├── assets/
│   │   ├── App.tsx
│   │   ├── main.tsx
│   │   └── index.css
│   ├── eslint.config.js
│   ├── .prettierrc
│   ├── vite.config.ts
│   ├── tsconfig.json
│   ├── tsconfig.app.json
│   ├── tsconfig.node.json
│   └── package.json
│
├── mobile-android/                    # Android (Java + Retrofit)
│   ├── app/
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/com/cognia/
│   │   │   │   │   ├── auth/          # Login, Splash, RoleRouter
│   │   │   │   │   ├── student/       # Student Screens
│   │   │   │   │   ├── parent/        # Parent's Screens
│   │   │   │   │   ├── network/       # Retrofit clients, interceptors
│   │   │   │   │   ├── models/        # DTO (match with backend)
│   │   │   │   │   ├── ui/            # Activities, Fragments, ViewModels
│   │   │   │   │   └── utils/
│   │   │   │   ├── res/
│   │   │   │   │   ├── layout/
│   │   │   │   │   ├── drawable/
│   │   │   │   │   ├── values/
│   │   │   │   │   └── ...
│   │   │   │   └── AndroidManifest.xml
│   │   │   └── test/
│   │   └── build.gradle
│   ├── build.gradle
│   ├── gradle.properties
│   ├── settings.gradle
│   └── gradlew / gradlew.bat
│
├── shared/                            # Shared resources
│   ├── api-contracts/                 # OpenAPI / JSON schemas
│   └── design-tokens/                 # Colors, fonts from Figma
│
├── infra/                             # DevOps
│   ├── docker-compose.yml
│   ├── nginx/
│   └── github-actions/
│
├── .gitignore
├── .editorconfig
├── README.md                          # ← general README (below)
└── CONTRIBUTING.md