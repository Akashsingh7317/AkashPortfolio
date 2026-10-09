package com.akash.portfolio.controller;

import com.akash.portfolio.service.CertificateService;
import com.akash.portfolio.service.ContactService;
import com.akash.portfolio.service.DashboardService;
import com.akash.portfolio.service.ExperienceService;
import com.akash.portfolio.service.ProjectService;
import com.akash.portfolio.service.SkillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final ProjectService projectService;
    private final SkillService skillService;
    private final ExperienceService experienceService;
    private final CertificateService certificateService;
    private final ContactService contactService;

    public DashboardController(
            DashboardService dashboardService,
            ProjectService projectService,
            SkillService skillService,
            ExperienceService experienceService,
            CertificateService certificateService,
            ContactService contactService) {

        this.dashboardService = dashboardService;
        this.projectService = projectService;
        this.skillService = skillService;
        this.experienceService = experienceService;
        this.certificateService = certificateService;
        this.contactService = contactService;
    }

    /*
     * Dashboard Home
     */

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
                "dashboard",
                dashboardService.getDashboardSummary());

        model.addAttribute(
                "totalProjects",
                dashboardService.totalProjects());

        model.addAttribute(
                "totalSkills",
                dashboardService.totalSkills());

        model.addAttribute(
                "totalExperience",
                dashboardService.totalExperience());

        model.addAttribute(
                "totalCertificates",
                dashboardService.totalCertificates());

        model.addAttribute(
                "totalVisitors",
                dashboardService.totalVisitors());

        model.addAttribute(
                "activeVisitors",
                dashboardService.activeVisitors());

        model.addAttribute(
                "totalContacts",
                dashboardService.totalContacts());

        model.addAttribute(
                "unreadMessages",
                dashboardService.unreadMessages());

        model.addAttribute(
                "featuredProjects",
                dashboardService.featuredProjects());

        model.addAttribute(
                "publishedProjects",
                dashboardService.publishedProjects());

        model.addAttribute(
                "projects",
                projectService.getPublishedProjects());

        model.addAttribute(
                "skills",
                skillService.getActiveSkills());

        model.addAttribute(
                "experiences",
                experienceService.getActiveExperience());

        model.addAttribute(
                "certificates",
                certificateService.getActiveCertificates());

        model.addAttribute(
                "contacts",
                contactService.getLatestContacts());

        model.addAttribute(
                "pageTitle",
                "Admin Dashboard");

        return "admin/dashboard";

    }

}