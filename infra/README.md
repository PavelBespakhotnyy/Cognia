# Cognia — Infrastructure

This folder contains all **environment and deployment configuration** for the Cognia project. It is intentionally kept separate from application code (`backend/`, `frontend-web/`, `mobile-android/`) so that infrastructure changes do not pollute business logic and vice versa.

---

## 📁 Structure

```
infra/
├── docker-compose.yml     # Local development services (PostgreSQL now, backend/nginx later)
├── nginx/                 # Reverse proxy configuration (added before deployment)
└── github-actions/        # CI/CD workflow drafts (real workflows live in .github/workflows/)
```

---

## 🎯 Why this folder exists

- **Isolation of concerns.** Application code and environment setup have different lifecycles and different owners.
- **Reproducibility.** Any team member can spin up an identical local environment with a single command.
- **Onboarding speed.** A new developer does not need to install PostgreSQL manually — Docker handles it.
- **Future-proofing.** When the project grows, `docker-compose.yml` will gain services for `backend` and `nginx`, and `nginx/` will hold production configs. Nothing needs to be moved around.

---

## 🐘 Current state: PostgreSQL for local development

Right now `docker-compose.yml` runs **only PostgreSQL 16**. This gives every developer the same database without touching their host system.

**Credentials (development only — never use in production):**

| Parameter | Value |
|-----------|-------|
| Host      | `localhost` |
| Port      | `5432` |
| Database  | `cognia` |
| User      | `cognia` |
| Password  | `cognia_dev_password` |

> ⚠️ These credentials are **local-only**. Production credentials will live in environment variables and will never be committed to the repository.

---

## 🚀 Main commands

All commands are run from the `infra/` directory.

### Start the database

```bash
docker compose up -d
```

Starts PostgreSQL in the background. The `-d` flag means "detached" — the terminal returns immediately.

### Check status

```bash
docker compose ps
```

Expected output: `cognia-postgres   Up (healthy)`. If the status is `starting`, wait 5–10 seconds and run the command again.

### View logs

```bash
docker compose logs -f postgres
```

`-f` follows the log output in real time. Press `Ctrl+C` to stop watching (the container keeps running).

### Stop the database (keep data)

```bash
docker compose down
```

Stops and removes containers, but the **volume `cognia_pgdata` is preserved** — your data survives.

### Stop and wipe all data

```bash
docker compose down -v
```

Also removes the volume. **All data is lost.** Use this when you want a clean slate (for example, before re-running Flyway migrations from scratch).

### Open a psql shell inside the container

```bash
docker exec -it cognia-postgres psql -U cognia -d cognia
```

Useful for quick inspection:

```sql
\dt      -- list tables
\q       -- quit
```

### Rebuild from scratch

```bash
docker compose down -v
docker compose up -d
```

---

## 🗄️ Important: database schema is NOT created here

This `docker-compose.yml` only starts an **empty PostgreSQL server with an empty database `cognia`**. Tables are **not** created manually.

The schema will be applied automatically by **Flyway migrations** located in:

```
backend/src/main/resources/db/migration/
```

When the Spring Boot backend starts, Flyway will:
1. Connect to the `cognia` database.
2. Check `flyway_schema_history` for already-applied migrations.
3. Apply any missing SQL files.

**Do not create tables manually** via DBeaver / pgAdmin / psql. The single source of truth is the SQL files in `db/migration/`. Manual changes will break Flyway and cause drift between environments.

---

## 🔮 Roadmap for this folder

| Item | When | Purpose |
|------|------|---------|
| `postgres` service | ✅ Done | Local database |
| `backend` service | When backend is dockerized | Run API + DB together with one command |
| `nginx` service | Before deployment | Reverse proxy: `/api` → backend, `/` → frontend |
| `nginx/nginx.conf` | Before deployment | HTTPS, gzip, proxying rules |
| `.env.example` | Before deployment | Template for environment variables |

> ⚠️ **Note on `github-actions/`:** GitHub Actions reads workflows from `.github/workflows/` at the repository root, **not** from `infra/github-actions/`. The folder here is a placeholder for drafts and shared scripts. Actual workflow files must be placed in `.github/workflows/` to run.

---

## 🛠️ Requirements

- **Docker Desktop** (Windows / macOS) or **Docker Engine + Compose plugin** (Linux).
- Verify with:
  ```bash
  docker --version
  docker compose version
  ```

Both should report a version (Docker 20+, Compose v2+).