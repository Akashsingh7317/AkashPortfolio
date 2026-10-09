package com.akash.portfolio.service;

import com.akash.portfolio.repository.CertificateRepository;
import com.akash.portfolio.repository.ContactRepository;
import com.akash.portfolio.repository.ExperienceRepository;
import com.akash.portfolio.repository.ProjectRepository;
import com.akash.portfolio.repository.SkillRepository;
import com.akash.portfolio.repository.VisitorRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final ExperienceRepository experienceRepository;
    private final CertificateRepository certificateRepository;
    private final ContactRepository contactRepository;
    private final VisitorRepository visitorRepository;

    public DashboardService(ProjectRepository projectRepository,
                            SkillRepository skillRepository,
                            ExperienceRepository experienceRepository,
                            CertificateRepository certificateRepository,
                            ContactRepository contactRepository,
                            VisitorRepository visitorRepository) {

        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
        this.experienceRepository = experienceRepository;
        this.certificateRepository = certificateRepository;
        this.contactRepository = contactRepository;
        this.visitorRepository = visitorRepository;
    }

    /*
     * Dashboard Summary
     */
    public Map<String, Long> getDashboardSummary() {

        Map<String, Long> dashboard = new LinkedHashMap<>();

        dashboard.put("totalProjects",
                projectRepository.count());

        dashboard.put("publishedProjects",
                projectRepository.countByPublishedTrue());

        dashboard.put("featuredProjects",
                projectRepository.countByFeaturedTrue());

        dashboard.put("totalSkills",
                skillRepository.count());

        dashboard.put("activeSkills",
                skillRepository.countByActiveTrue());

        dashboard.put("totalExperience",
                experienceRepository.count());

        dashboard.put("activeExperience",
                experienceRepository.countByActiveTrue());

        dashboard.put("totalCertificates",
                certificateRepository.count());

        dashboard.put("activeCertificates",
                certificateRepository.countByActiveTrue());

        dashboard.put("totalContacts",
                contactRepository.count());

        dashboard.put("unreadMessages",
                contactRepository.countByReadStatusFalse());

        dashboard.put("totalVisitors",
                visitorRepository.count());

        dashboard.put("activeVisitors",
                visitorRepository.countByActiveSessionTrue());

        return dashboard;

    }

    /*
     * Dashboard Cards
     */
    public long totalProjects() {
        return projectRepository.count();
    }

    public long totalSkills() {
        return skillRepository.count();
    }

    public long totalExperience() {
        return experienceRepository.count();
    }

    public long totalCertificates() {
        return certificateRepository.count();
    }

    public long totalContacts() {
        return contactRepository.count();
    }

    public long unreadMessages() {
        return contactRepository.countByReadStatusFalse();
    }

    public long totalVisitors() {
        return visitorRepository.count();
    }

    public long activeVisitors() {
        return visitorRepository.countByActiveSessionTrue();
    }

    public long featuredProjects() {
        return projectRepository.countByFeaturedTrue();
    }

    public long publishedProjects() {
        return projectRepository.countByPublishedTrue();
    }

}