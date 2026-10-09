package com.akash.portfolio.config;

import com.akash.portfolio.entity.Admin;
import com.akash.portfolio.entity.Project;
import com.akash.portfolio.entity.Skill;
import com.akash.portfolio.repository.AdminRepository;
import com.akash.portfolio.repository.ProjectRepository;
import com.akash.portfolio.repository.SkillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/*
 * First-run content: the projects from github.com/Akashsingh7317,
 * the skill set and an admin account. Each table is seeded only
 * while it is empty, so anything edited in the admin panel stays.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private static final String GITHUB = "https://github.com/Akashsingh7317/";

    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final PortfolioProperties profile;

    @Value("${portfolio.admin.username:admin}")
    private String adminUsername;

    @Value("${portfolio.admin.password:}")
    private String adminPassword;

    public DataSeeder(ProjectRepository projectRepository,
                      SkillRepository skillRepository,
                      AdminRepository adminRepository,
                      PasswordEncoder passwordEncoder,
                      PortfolioProperties profile) {

        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.profile = profile;

    }

    @Override
    public void run(String... args) {

        if (projectRepository.count() == 0) {
            seedProjects();
        }

        if (skillRepository.count() == 0) {
            seedSkills();
        }

        if (adminRepository.count() == 0) {
            seedAdmin();
        }

    }

    // =====================================================
    // Projects
    // =====================================================

    private void seedProjects() {

        Project rag = project(1, "PDF RAG Agent", "pdf-rag-agent", "AI / ML",
                "Chat with your PDFs, Word files and photos: hybrid search, cited answers, OCR, voice and Hinglish. "
                        + "Production-style stack with Celery workers, JWT auth and Docker.",
                "Python, Flask, LangChain, Groq / OpenAI, ChromaDB, BM25, Celery, Redis, MySQL, Alembic, JWT, Docker, GitHub Actions");
        rag.setDescription(
                "A document assistant that answers questions only from your own files and cites the file and page "
                        + "for every fact. It handles large PDFs (500+ pages, including scanned ones), Word files, "
                        + "text files and photos. You can ask one document or all of them at once, by typing or by "
                        + "voice, in English or Hinglish, and answers stream in as they are written.\n\n"
                        + "Retrieval combines vector search (meaning) with BM25 keyword search (exact codes, numbers, "
                        + "names) merged with Reciprocal Rank Fusion. An evaluation harness of 30 questions with known "
                        + "answer pages measures it: hybrid search reaches 100% Hit@1, versus 97% for vector search and "
                        + "80% for keyword search alone.");
        rag.setKeyFeatures(String.join("\n",
                "Hybrid retrieval: vector search + BM25 merged with Reciprocal Rank Fusion",
                "Cited answers: every fact links to the source passage and opens the PDF at that page",
                "OCR for scanned PDFs and photos using a vision model",
                "Voice questions with Whisper speech-to-text",
                "Hinglish questions get Hinglish answers (script detected in code)",
                "Follow-up questions rewritten into standalone queries from chat history",
                "JWT auth in HttpOnly cookies with CSRF protection; per-user data isolation",
                "Celery + Redis background ingestion with live progress",
                "Docker Compose stack (web, worker, MySQL, Redis, Chroma) and GitHub Actions CI",
                "92 offline tests that run the full pipeline with fake models"));
        rag.setChallenges(
                "The first hybrid version scored only 83% Hit@1, worse than vector search alone: common words "
                        + "(like \"printer\" in a printer manual) gave BM25 a full but meaningless ranking that "
                        + "pushed correct pages down.");
        rag.setSolution(
                "Keyword search now uses only distinctive query words (no stopwords, present in at most half of the "
                        + "chunks), which brought hybrid retrieval to 100% Hit@1 on the evaluation set, with 30/30 "
                        + "generated answers containing the expected facts.");
        rag.setRole("Solo developer");
        rag.setFeatured(true);

        Project mmep = project(2, "MMEP – Education Management System", "mmep-education-management", "Full Stack",
                "Full-stack education platform with separate Admin, Teacher and Student portals: courses, batches, "
                        + "attendance, assignments, quizzes, tests and results.",
                "React, Spring Boot, Spring Security, JWT, Spring Data JPA, MySQL, REST APIs");
        mmep.setGithubUrl(GITHUB + "mmep-full-stack-project");
        mmep.setDescription(
                "MMEP is a complete institute management system built as a React single-page app on top of a Spring "
                        + "Boot REST API. Each role gets its own portal: admins manage courses, batches, students, "
                        + "teachers, notices, banners, enquiries and testimonials; teachers run their batches, mark "
                        + "attendance, create assignments, quizzes and tests and review submissions; students follow "
                        + "their courses, study material, attempt quizzes and tests and see their results.\n\n"
                        + "Authentication uses JWT with role-based route protection on both the API and the React "
                        + "router, plus email-based forgot/reset password.");
        mmep.setKeyFeatures(String.join("\n",
                "Three role-based portals: Admin, Teacher and Student",
                "JWT authentication with protected React routes",
                "Course, module, lesson and batch management",
                "Attendance tracking and assignment submissions",
                "Online quizzes and tests with results",
                "Notices, notifications, banners, FAQs and testimonials",
                "Enquiry management and certificates",
                "Forgot / reset password over email",
                "Global exception handling with a consistent API response format"));
        mmep.setRole("Full stack developer");
        mmep.setFeatured(true);

        Project naukri = project(3, "NaukriHub – Job Portal", "naukrihub-job-portal", "Full Stack",
                "A Naukri.com-style job portal with job seeker and employer roles: job search with filters, "
                        + "applications with cover letters and status tracking.",
                "Java 17, Spring Boot, Spring Security, Thymeleaf, Spring Data JPA, MySQL, HTML, CSS, JavaScript, Docker");
        naukri.setGithubUrl(GITHUB + "naukri-clone");
        naukri.setDescription(
                "NaukriHub is a server-rendered job portal built with Spring Boot and Thymeleaf. Job seekers build a "
                        + "profile (education, employment, IT skills, projects), search jobs by keyword, location and "
                        + "category and apply with a cover letter. Employers post, edit and delete jobs and move "
                        + "applicants through the pipeline: Applied, Shortlisted, Interview, Selected or Rejected.");
        naukri.setKeyFeatures(String.join("\n",
                "Job search with keyword, location and category filters",
                "Separate job seeker and employer registration and login",
                "Employers post, edit and delete jobs and view applicants",
                "Apply with cover letter; status tracking from Applied to Selected",
                "Detailed seeker profile: education, employment, skills, projects, resume upload",
                "Spring Security with BCrypt password hashing",
                "Responsive UI and Dockerfile for deployment"));
        naukri.setRole("Full stack developer");
        naukri.setFeatured(true);

        Project portfolio = project(4, "Portfolio CMS", "portfolio-cms", "Full Stack",
                "This website: a Spring Boot portfolio with its own admin panel to manage projects, skills, "
                        + "experience, certificates, uploads and contact messages.",
                "Java, Spring Boot, Spring Security, Thymeleaf, Spring Data JPA, PostgreSQL, JavaScript");
        portfolio.setDescription(
                "The portfolio you are looking at. Public pages are rendered with Thymeleaf from a PostgreSQL "
                        + "database, and a secured admin panel lets me add or edit projects, skills, experience and "
                        + "certificates, upload images, videos and a resume, and read messages sent from the contact "
                        + "form. The GitHub section is loaded live from the GitHub API, so new repositories appear "
                        + "automatically.");
        portfolio.setKeyFeatures(String.join("\n",
                "Admin CMS for projects, skills, experience and certificates",
                "Secure admin login with Spring Security and BCrypt",
                "File uploads for thumbnails, galleries, demo videos and resume",
                "Contact form that stores messages and sends email notifications",
                "Live GitHub repositories and stats from the GitHub API",
                "Responsive design with light and dark themes"));
        portfolio.setRole("Solo developer");

        Project health = project(5, "Health Monitor Portal", "health-monitor-portal", "Backend",
                "Spring Boot web app where users sign up, log in and record health metrics over time.",
                "Java, Spring Boot, Spring Data JPA, Thymeleaf, MySQL, Docker");
        health.setGithubUrl(GITHUB + "HealthMonitorPortal");
        health.setDescription(
                "A layered Spring Boot application (controller, service, repository) for tracking personal health "
                        + "data. Users register and log in, then record metrics such as weight, blood pressure or "
                        + "heart rate with a timestamp; each record is linked to its user through a JPA "
                        + "many-to-one relationship.");
        health.setKeyFeatures(String.join("\n",
                "User sign-up and login",
                "Record health metrics with timestamps",
                "Per-user health history (JPA @ManyToOne)",
                "Layered architecture: controller, service, repository",
                "Dockerfile for containerised deployment"));
        health.setRole("Backend developer");

        rag.setGithubUrl(GITHUB + "pdf-rag-agent");

        projectRepository.save(rag);
        projectRepository.save(mmep);
        projectRepository.save(naukri);
        projectRepository.save(portfolio);
        projectRepository.save(health);

        log.info("Seeded 5 projects from GitHub.");

    }

    private Project project(int order, String title, String slug, String category,
                            String shortDescription, String technologies) {

        Project p = new Project();
        p.setTitle(title);
        p.setSlug(slug);
        p.setCategory(category);
        p.setShortDescription(shortDescription);
        p.setTechnologies(technologies);
        p.setProjectStatus("Completed");
        p.setPublished(true);
        p.setDisplayOrder(order);
        return p;

    }

    // =====================================================
    // Skills
    // =====================================================

    private void seedSkills() {

        String[][] skills = {
                // name, category, icon class (devicon or Font Awesome), level
                {"Java", "Backend", "devicon-java-plain colored", "90"},
                {"Spring Boot", "Backend", "devicon-spring-original colored", "88"},
                {"Spring Security & JWT", "Backend", "fa-solid fa-shield-halved", "82"},
                {"Spring Data JPA / Hibernate", "Backend", "devicon-hibernate-plain colored", "82"},
                {"REST API Design", "Backend", "fa-solid fa-plug", "88"},

                {"Python", "AI / ML & Python", "devicon-python-plain colored", "80"},
                {"LLMs (OpenAI, Groq, Llama)", "AI / ML & Python", "fa-solid fa-brain", "78"},
                {"LangChain", "AI / ML & Python", "fa-solid fa-link", "75"},
                {"RAG (Retrieval-Augmented Generation)", "AI / ML & Python", "fa-solid fa-magnifying-glass-chart", "78"},
                {"Vector Databases (ChromaDB)", "AI / ML & Python", "fa-solid fa-database", "72"},
                {"Embeddings & Semantic Search", "AI / ML & Python", "fa-solid fa-diagram-project", "72"},
                {"Prompt Engineering", "AI / ML & Python", "fa-solid fa-terminal", "78"},
                {"Speech-to-Text (Whisper)", "AI / ML & Python", "fa-solid fa-microphone-lines", "65"},
                {"LLM Evaluation (Hit@k, MRR)", "AI / ML & Python", "fa-solid fa-chart-line", "68"},
                {"Flask", "AI / ML & Python", "devicon-flask-original", "75"},

                {"React", "Frontend", "devicon-react-original colored", "75"},
                {"JavaScript (ES6+)", "Frontend", "devicon-javascript-plain colored", "80"},
                {"HTML5", "Frontend", "devicon-html5-plain colored", "90"},
                {"CSS3", "Frontend", "devicon-css3-plain colored", "85"},
                {"Thymeleaf", "Frontend", "fa-solid fa-leaf", "82"},
                {"Bootstrap", "Frontend", "devicon-bootstrap-plain colored", "80"},

                {"MySQL", "Databases", "devicon-mysql-original colored", "85"},
                {"PostgreSQL", "Databases", "devicon-postgresql-plain colored", "78"},
                {"Redis", "Databases", "devicon-redis-plain colored", "65"},
                {"SQL & Data Modelling", "Databases", "fa-solid fa-table", "82"},

                {"Git & GitHub", "Tools & DevOps", "devicon-git-plain colored", "85"},
                {"Docker & Compose", "Tools & DevOps", "devicon-docker-plain colored", "72"},
                {"GitHub Actions CI", "Tools & DevOps", "devicon-githubactions-plain colored", "65"},
                {"Maven", "Tools & DevOps", "devicon-maven-plain colored", "80"},
                {"Postman", "Tools & DevOps", "devicon-postman-plain colored", "85"},
                {"Pytest", "Tools & DevOps", "fa-solid fa-vial-circle-check", "68"},
        };

        int order = 1;

        for (String[] s : skills) {

            Skill skill = new Skill();
            skill.setSkillName(s[0]);
            skill.setCategory(s[1]);
            skill.setIcon(s[2]);
            skill.setPercentage(Integer.parseInt(s[3]));
            skill.setDisplayOrder(order++);
            skill.setActive(true);
            skill.setFeatured(true);
            skillRepository.save(skill);

        }

        log.info("Seeded {} skills.", skills.length);

    }

    // =====================================================
    // Admin
    // =====================================================

    private void seedAdmin() {

        boolean generated = adminPassword == null || adminPassword.isBlank();

        String password = generated
                ? UUID.randomUUID().toString().substring(0, 12)
                : adminPassword;

        Admin admin = new Admin();
        admin.setFullName(profile.getName());
        admin.setUsername(adminUsername);
        admin.setEmail(profile.getEmail());
        admin.setPassword(passwordEncoder.encode(password));
        adminRepository.save(admin);

        if (generated) {
            log.warn("Created admin account '{}' with generated password: {}  "
                    + "(change it in Admin > Change Password)", adminUsername, password);
        } else {
            log.info("Created admin account '{}'.", adminUsername);
        }

    }

}
