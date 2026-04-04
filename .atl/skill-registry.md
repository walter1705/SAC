# Skill Registry — SAC

Generated: 2026-04-04

## User Skills

| Skill | Trigger | Path |
|-------|---------|------|
| next-developer | Next.js/React/Bun frontend work | `~/.claude/skills/next-developer/SKILL.md` |
| branch-pr | Creating pull requests | `~/.claude/skills/branch-pr/SKILL.md` |
| issue-creation | Creating GitHub issues | `~/.claude/skills/issue-creation/SKILL.md` |
| judgment-day | Adversarial dual review | `~/.claude/skills/judgment-day/SKILL.md` |
| go-testing | Go tests, Bubbletea TUI | `~/.claude/skills/go-testing/SKILL.md` |
| skill-creator | Creating new AI skills | `~/.claude/skills/skill-creator/SKILL.md` |

## Project Conventions

| File | Purpose |
|------|---------|
| `CLAUDE.md` | Project instructions — SAC architecture, commands, domain language |

## Compact Rules

### next-developer
- Runtime: Bun. Framework: Next.js App Router.
- Server Components by default. `'use client'` only when needed (useState, useEffect, browser hooks).
- Data fetching in Server Components via native fetch.
- Forms: Server Actions + Zod validation (client + server).
- Styling: Tailwind CSS. Components: shadcn/ui (Radix). Icons: Lucide React. Animations: Framer Motion.
- No 500-line files. Break UI into small reusable components in /components.
- Design: modern, minimalist, premium SaaS style. High contrast, clean typography, micro-interactions.
- Dark/light theme support.

### branch-pr
- Every PR MUST link an approved issue.
- Every PR MUST have exactly one `type:*` label.
- Automated checks must pass before merge.

### issue-creation
- MUST use a template (bug report or feature request). No blank issues.
- Every issue gets `status:needs-review` on creation.
- Maintainer MUST add `status:approved` before any PR can be opened.
