package com.akash.portfolio.controller;

import com.akash.portfolio.entity.Project;
import com.akash.portfolio.service.ProjectService;
import com.akash.portfolio.service.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UploadService uploadService;

    public ProjectController(ProjectService projectService,
                             UploadService uploadService) {

        this.projectService = projectService;
        this.uploadService = uploadService;

    }

    /*
     * Project List
     */

    @GetMapping
    public String allProjects(Model model) {

        model.addAttribute(
                "projects",
                projectService.getAll());

        return "admin/project/list";

    }

    /*
     * Add Project Page
     */

    @GetMapping("/add")
    public String addProjectPage(Model model) {

        model.addAttribute(
                "project",
                new Project());

        return "admin/project/add";

    }

    /*
     * Save Project
     */

    @PostMapping("/save")
    public String saveProject(

            @ModelAttribute Project project,

            @RequestParam(value = "thumbnailFile",
                    required = false)
            MultipartFile thumbnail,

            @RequestParam(value = "demoVideoFile",
                    required = false)
            MultipartFile demoVideo

    ) {

        try {

            if (thumbnail != null &&
                    !thumbnail.isEmpty()) {

                String thumbnailPath =
                        uploadService.uploadImage(
                                thumbnail,
                                "projects");

                project.setThumbnail(thumbnailPath);

            }

            if (demoVideo != null &&
                    !demoVideo.isEmpty()) {

                String videoPath =
                        uploadService.uploadVideo(
                                demoVideo);

                project.setDemoVideo(videoPath);

            }

            projectService.create(project);

            return "redirect:/admin/projects?success";

        }

        catch (Exception e) {

            return "redirect:/admin/projects/add?error";

        }

    }

    /*
     * Edit Project
     */

    @GetMapping("/edit/{id}")
    public String editProject(

            @PathVariable Long id,

            Model model

    ) {

        model.addAttribute(

                "project",

                projectService.getById(id)

        );

        return "admin/project/edit";

    }
    
    /*
     * Update Project
     */
    @PostMapping("/update/{id}")
    public String updateProject(

            @PathVariable Long id,

            @ModelAttribute Project project,

            @RequestParam(value = "thumbnailFile", required = false)
            MultipartFile thumbnail,

            @RequestParam(value = "demoVideoFile", required = false)
            MultipartFile demoVideo

    ) {

        try {

            Project existingProject =
                    projectService.getById(id);

            if (thumbnail != null && !thumbnail.isEmpty()) {

                String thumbnailPath =
                        uploadService.uploadImage(
                                thumbnail,
                                "projects");

                existingProject.setThumbnail(thumbnailPath);

            }

            if (demoVideo != null && !demoVideo.isEmpty()) {

                String videoPath =
                        uploadService.uploadVideo(
                                demoVideo);

                existingProject.setDemoVideo(videoPath);

            }

            existingProject.setTitle(project.getTitle());
            existingProject.setShortDescription(project.getShortDescription());
            existingProject.setDescription(project.getDescription());
            existingProject.setCategory(project.getCategory());
            existingProject.setTechnologies(project.getTechnologies());
            existingProject.setGithubUrl(project.getGithubUrl());
            existingProject.setLiveUrl(project.getLiveUrl());
            existingProject.setProjectStatus(project.getProjectStatus());
            existingProject.setFeatured(project.isFeatured());
            existingProject.setPublished(project.isPublished());
            existingProject.setDisplayOrder(project.getDisplayOrder());
            existingProject.setSeoTitle(project.getSeoTitle());
            existingProject.setSeoDescription(project.getSeoDescription());
            existingProject.setClient(project.getClient());
            existingProject.setDuration(project.getDuration());
            existingProject.setRole(project.getRole());
            existingProject.setKeyFeatures(project.getKeyFeatures());
            existingProject.setChallenges(project.getChallenges());
            existingProject.setSolution(project.getSolution());
            existingProject.setFutureScope(project.getFutureScope());

            projectService.update(id, existingProject);

            return "redirect:/admin/projects?updated";

        }

        catch (Exception e) {

            return "redirect:/admin/projects?error";

        }

    }

    /*
     * Delete Project
     */

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id) {

        projectService.delete(id);

        return "redirect:/admin/projects?deleted";

    }

    /*
     * View Project
     */

    @GetMapping("/view/{id}")
    public String viewProject(

            @PathVariable Long id,

            Model model

    ) {

        model.addAttribute(
                "project",
                projectService.getById(id));

        return "admin/project/view";

    }

    /*
     * Public Project Details
     */

    @GetMapping("/details/{slug}")
    public String projectDetails(

            @PathVariable String slug,

            Model model

    ) {

        model.addAttribute(
                "project",
                projectService.getBySlug(slug));

        return "project-details";

    }

    /*
     * Publish Project
     */

    @GetMapping("/publish/{id}")
    public String publishProject(
            @PathVariable Long id) {

        Project project =
                projectService.getById(id);

        project.setPublished(true);

        projectService.update(id, project);

        return "redirect:/admin/projects";

    }

    /*
     * Unpublish Project
     */

    @GetMapping("/unpublish/{id}")
    public String unpublishProject(
            @PathVariable Long id) {

        Project project =
                projectService.getById(id);

        project.setPublished(false);

        projectService.update(id, project);

        return "redirect:/admin/projects";

    }
    /*
     * Feature Project
     */
    @GetMapping("/feature/{id}")
    public String featureProject(
            @PathVariable Long id) {

        Project project = projectService.getById(id);

        project.setFeatured(true);

        projectService.update(id, project);

        return "redirect:/admin/projects";

    }

    /*
     * Remove Featured
     */
    @GetMapping("/remove-feature/{id}")
    public String removeFeaturedProject(
            @PathVariable Long id) {

        Project project = projectService.getById(id);

        project.setFeatured(false);

        projectService.update(id, project);

        return "redirect:/admin/projects";

    }

    /*
     * Search Project
     */
    @GetMapping("/search")
    public String searchProject(

            @RequestParam("keyword") String keyword,

            Model model

    ) {

        model.addAttribute(
                "projects",
                projectService.search(keyword));

        model.addAttribute(
                "keyword",
                keyword);

        return "admin/project/list";

    }

    /*
     * Project Pagination
     */
    @GetMapping("/page/{page}")
    public String projectPagination(

            @PathVariable int page,

            Model model

    ) {

        model.addAttribute(
                "projects",
                projectService.getProjects(page));

        model.addAttribute(
                "currentPage",
                page);

        return "admin/project/list";

    }

    /*
     * Upload Gallery Images
     */
    @PostMapping("/gallery/{id}")
    public String uploadGallery(

            @PathVariable Long id,

            @RequestParam("images")
            MultipartFile[] images

    ) {

        try {

            projectService.uploadGallery(
                    id,
                    images);

            return "redirect:/admin/projects/view/" + id;

        }

        catch (Exception e) {

            return "redirect:/admin/projects?galleryError";

        }

    }

    /*
     * Dashboard Statistics
     */
    @GetMapping("/statistics")
    public String statistics(Model model) {

        model.addAttribute(
                "totalProjects",
                projectService.totalProjects());

        model.addAttribute(
                "publishedProjects",
                projectService.totalPublished());

        model.addAttribute(
                "featuredProjects",
                projectService.totalFeatured());

        model.addAttribute(
                "draftProjects",
                projectService.totalDraft());

        return "admin/project/statistics";

    }

}