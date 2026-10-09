package com.akash.portfolio.controller;

import com.akash.portfolio.config.PortfolioProperties;
import com.akash.portfolio.dto.ContactDTO;
import com.akash.portfolio.entity.Project;
import com.akash.portfolio.entity.Skill;
import com.akash.portfolio.service.CertificateService;
import com.akash.portfolio.service.ExperienceService;
import com.akash.portfolio.service.ProjectService;
import com.akash.portfolio.service.SkillService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class HomeController {

    private final ProjectService projectService;
    private final SkillService skillService;
    private final ExperienceService experienceService;
    private final CertificateService certificateService;
    private final PortfolioProperties profile;
    private final String uploadDir;

    public HomeController(ProjectService projectService,
                          SkillService skillService,
                          ExperienceService experienceService,
                          CertificateService certificateService,
                          PortfolioProperties profile,
                          @Value("${file.upload-dir}") String uploadDir) {

        this.projectService = projectService;
        this.skillService = skillService;
        this.experienceService = experienceService;
        this.certificateService = certificateService;
        this.profile = profile;
        this.uploadDir = uploadDir;

    }

    /*
     * Home Page (single-page portfolio)
     */
    @GetMapping("/")
    public String home(Model model) {

        addProfile(model);

        List<Project> projects = projectService.getPublishedProjects();

        model.addAttribute("projects", projects);

        model.addAttribute("projectCount", projects.size());

        model.addAttribute(
                "projectCategories",
                projects.stream()
                        .map(Project::getCategory)
                        .filter(c -> c != null && !c.isBlank())
                        .distinct()
                        .toList());

        // The first featured project is shown large, with its key features.
        projects.stream()
                .filter(Project::isFeatured)
                .findFirst()
                .ifPresent(p -> {
                    model.addAttribute("spotlightId", p.getId());
                    model.addAttribute(
                            "spotlightFeatures",
                            splitList(p.getKeyFeatures(), "\\R").stream().limit(4).toList());
                });

        List<Skill> skills = skillService.getActiveSkills();

        Map<String, List<Skill>> skillGroups = skills.stream()
                .collect(Collectors.groupingBy(
                        Skill::getCategory,
                        LinkedHashMap::new,
                        Collectors.toList()));

        model.addAttribute("skillGroups", skillGroups);

        model.addAttribute("skillCount", skills.size());

        model.addAttribute("skillList", skills);

        model.addAttribute(
                "experiences",
                experienceService.getActiveExperience());

        model.addAttribute(
                "certificates",
                certificateService.getActiveCertificates());

        if (!model.containsAttribute("contact")) {
            model.addAttribute("contact", new ContactDTO());
        }

        return "index";

    }

    /*
     * Public Project Details
     */
    @GetMapping("/project/{slug}")
    public String projectDetails(@PathVariable String slug, Model model) {

        Project project;

        try {
            project = projectService.getBySlug(slug);
        } catch (RuntimeException e) {
            return "redirect:/#projects";
        }

        if (!project.isPublished()) {
            return "redirect:/#projects";
        }

        addProfile(model);

        model.addAttribute("project", project);

        model.addAttribute("technologies", splitList(project.getTechnologies(), ","));

        model.addAttribute("features", splitList(project.getKeyFeatures(), "\\R"));

        model.addAttribute(
                "gallery",
                Stream.of(project.getImage1(), project.getImage2(), project.getImage3(),
                                project.getImage4(), project.getImage5())
                        .filter(img -> img != null && !img.isBlank())
                        .toList());

        model.addAttribute(
                "otherProjects",
                projectService.getPublishedProjects().stream()
                        .filter(p -> !p.getId().equals(project.getId()))
                        .limit(3)
                        .toList());

        return "project-details";

    }

    /*
     * Old multi-page URLs now point at sections of the home page.
     */
    @GetMapping({"/about", "/projects", "/skills", "/experience", "/certificates"})
    public String sectionRedirect(HttpServletRequest request) {

        return "redirect:/#" + request.getRequestURI().substring(1);

    }

    private static List<String> splitList(String value, String separatorRegex) {

        if (value == null || value.isBlank()) {
            return List.of();
        }

        return Arrays.stream(value.split(separatorRegex))
                .map(String::trim)
                .map(s -> s.startsWith("- ") ? s.substring(2) : s)
                .filter(s -> !s.isEmpty())
                .toList();

    }

    private void addProfile(Model model) {

        model.addAttribute("profile", profile);

        boolean hasResume = profile.getResumeFile() != null
                && Files.exists(Paths.get(uploadDir, profile.getResumeFile()));

        model.addAttribute(
                "resumeUrl",
                hasResume ? "/uploads/" + profile.getResumeFile() : null);

    }

}
