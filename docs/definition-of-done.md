# Definition of Done (DoD) — Cognia

This document defines the criteria that any task must meet to be
considered "done" in the Cognia project. It applies to all tasks in the
backlog, regardless of their type (feature, documentation,
infrastructure, or design).

## General Criteria

A task is considered done when it meets **all** of the following
points:

1. **Code or Deliverable Completed.** The functionality, document, or
   artifact is finalized as described in the Issue.

2. **Peer Review.** The change has been incorporated via a Pull Request
   and has been reviewed and approved by at least one team member other
   than the author.

3. **No Conflicts.** The branch is up-to-date with `main` and does not
   generate merge conflicts.

4. **Tests Passed.** If the task involves code:
   - Unit tests pass successfully.
   - Integration tests for the affected flow pass.
   - No errors have been introduced into existing functionalities.

5. **Documentation Updated.** If the task affects the architecture, API,
   or system usage, the corresponding documentation (LaTeX memory,
   README, Swagger) has been updated.

6. **Issue Closed and Linked.** The Issue is closed on GitHub, with a
   link to the corresponding Pull Request and a brief description of
   what was done.

7. **Milestone Updated.** The Issue is linked to the corresponding
   sprint Milestone and appears as closed on the Kanban board (column
   "Done").

## Specific Criteria by Task Type

### Development Tasks (backend, web, Android)

- The code compiles without critical errors or warnings.
- Comments have been added where the logic is not evident.
- The complete affected flow has been manually tested.
- The API responds according to the OpenAPI/Swagger specification.

### Documentation Tasks (LaTeX, README, manuals)

- The document compiles without errors (in the case of LaTeX).
- The content is written in Spanish and reviewed by the other team
  member.
- References, links, and diagrams are up-to-date.

### Infrastructure Tasks (GitHub, VPS, CI/CD)

- The configuration is documented in the repository.
- Functionality has been verified in a clean environment.
- A rollback plan exists in case of failure.

### Design Tasks (UX/UI, wireframes, Figma)

- The design covers all states (empty, loading, error, success).
- It has been validated with at least one real user or with the team.
- Resources are exported and accessible in `/design`.

## Exceptions

If a task cannot meet a specific criterion for justified reasons (lack
of time, external dependency, scope change), the exception will be
documented in the Issue itself with the corresponding justification and
the approval of the other team member.

---

**Version:** 1.0
**Date:** 19/10/2026
**Team:** Pavel Bespakhotnyi, Alla Kolchanova