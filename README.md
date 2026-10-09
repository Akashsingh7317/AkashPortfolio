# Akash Singh — Portfolio

Personal portfolio and CMS built with **Java 17, Spring Boot 3, Thymeleaf and PostgreSQL**.
A cinematic, dark single-page site (spotlight hero, 3D profile card, project carousel, live GitHub
section) backed by a secured admin panel for managing the content.

## Features

- **Portfolio site** — loader, rotating hero headline with cursor parallax, 3D pop-out profile card,
  auto-playing project carousel, skills marquee, roadmap and a contact form with a live JSON preview.
- **Project case studies** — `/project/{slug}` pages with overview, features, challenge/solution and stack.
- **Live GitHub** — repositories, languages and last update loaded from the GitHub API in the browser.
- **Admin CMS** (`/admin/login`) — manage projects, skills, experience, certificates, uploads and
  contact messages. Spring Security with BCrypt.
- **Contact form** — messages are stored in the database and emailed (when SMTP is configured).
- **First-run seeding** — projects and skills are inserted when the tables are empty; later edits
  through the admin panel are kept.

## Tech stack

Java 17 · Spring Boot 3.5 · Spring MVC · Spring Security · Spring Data JPA / Hibernate · Thymeleaf ·
PostgreSQL · Maven · vanilla JavaScript & CSS

## Run locally

Requirements: Java 17+, Maven, PostgreSQL.

```bash
# 1. create the database
psql -U postgres -c "CREATE DATABASE portfolio;"

# 2. start the app (http://localhost:9090)
mvn spring-boot:run
```

Configuration comes from environment variables, with local defaults in `application.properties`:

| Variable | Purpose | Default |
|---|---|---|
| `DB_USERNAME` / `DB_PASSWORD` | PostgreSQL login | `postgres` / `root` |
| `PORT` | HTTP port | `9090` |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | Gmail address and App Password for contact emails | — |
| `ADMIN_USERNAME` / `ADMIN_PASSWORD` | Admin account created on first run | `admin` / generated (printed once in the log) |

Profile details shown on the site (name, role, links, location) live under `portfolio.*`
in `src/main/resources/application.properties`.

## Contact

- GitHub: [Akashsingh7317](https://github.com/Akashsingh7317)
- LinkedIn: [akash7317](https://www.linkedin.com/in/akash7317)
- Email: akashsingh16423@gmail.com
