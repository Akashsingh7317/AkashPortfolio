package com.akash.portfolio.service;

import com.akash.portfolio.entity.Project;
import com.akash.portfolio.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final UploadService uploadService;

    public ProjectService(ProjectRepository repository,
                          UploadService uploadService) {

        this.repository = repository;
        this.uploadService = uploadService;

    }

    // =====================================
    // CREATE PROJECT
    // =====================================

    public Project create(Project project) {

        project.setSlug(generateSlug(project.getTitle()));

        if (project.getProjectStatus() == null
                || project.getProjectStatus().isBlank()) {

            project.setProjectStatus("Coming Soon");

        }

        return repository.save(project);

    }

    // =====================================
    // UPDATE PROJECT
    // =====================================

    public Project update(Long id,
                          Project request) {

        Project project = getById(id);

        project.setTitle(request.getTitle());

        project.setSlug(generateSlug(request.getTitle()));

        project.setShortDescription(
                request.getShortDescription());

        project.setDescription(
                request.getDescription());

        project.setCategory(
                request.getCategory());

        project.setTechnologies(
                request.getTechnologies());

        project.setThumbnail(
                request.getThumbnail());

        project.setImage1(
                request.getImage1());

        project.setImage2(
                request.getImage2());

        project.setImage3(
                request.getImage3());

        project.setImage4(
                request.getImage4());

        project.setImage5(
                request.getImage5());

        project.setDemoVideo(
                request.getDemoVideo());

        project.setGithubUrl(
                request.getGithubUrl());

        project.setLiveUrl(
                request.getLiveUrl());

        project.setProjectStatus(
                request.getProjectStatus());

        project.setFeatured(
                request.isFeatured());

        project.setPublished(
                request.isPublished());

        project.setDisplayOrder(
                request.getDisplayOrder());

        project.setSeoTitle(
                request.getSeoTitle());

        project.setSeoDescription(
                request.getSeoDescription());

        project.setClient(
                request.getClient());

        project.setDuration(
                request.getDuration());

        project.setRole(
                request.getRole());

        project.setKeyFeatures(
                request.getKeyFeatures());

        project.setChallenges(
                request.getChallenges());

        project.setSolution(
                request.getSolution());

        project.setFutureScope(
                request.getFutureScope());

        return repository.save(project);

    }

    // =====================================
    // DELETE PROJECT
    // =====================================

    public void delete(Long id) {

        Project project = getById(id);

        repository.delete(project);

    }

    // =====================================
    // FIND BY ID
    // =====================================

    public Project getById(Long id) {

        return repository.findById(id)

                .orElseThrow(() ->
                        new RuntimeException(
                                "Project not found"));

    }

    // =====================================
    // FIND BY SLUG
    // =====================================

    public Project getBySlug(String slug) {

        return repository.findBySlug(slug)

                .orElseThrow(() ->
                        new RuntimeException(
                                "Project not found"));

    }

    // =====================================
    // GET ALL PROJECTS
    // =====================================

    public List<Project> getAll() {

        return repository.findAll(

                Sort.by("displayOrder")
                        .ascending()

        );

    }

    // =====================================
    // PUBLISHED PROJECTS
    // =====================================

    public List<Project> getPublishedProjects() {

        return repository
                .findByPublishedTrueOrderByDisplayOrderAsc();

    }

    // =====================================
    // FEATURED PROJECTS
    // =====================================

    public List<Project> getFeaturedProjects() {

        return repository
                .findByFeaturedTrueOrderByDisplayOrderAsc();

    }
    // =====================================
    // SEARCH PROJECT
    // =====================================

    public List<Project> search(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {

            return repository.findAll(
                    Sort.by("displayOrder")
                            .ascending());

        }

        String search = keyword.toLowerCase(Locale.ENGLISH);

        return repository.findAll()

                .stream()

                .filter(project ->

                        (project.getTitle() != null &&
                                project.getTitle().toLowerCase(Locale.ENGLISH).contains(search))

                                ||

                                (project.getCategory() != null &&
                                        project.getCategory().toLowerCase(Locale.ENGLISH).contains(search))

                                ||

                                (project.getTechnologies() != null &&
                                        project.getTechnologies().toLowerCase(Locale.ENGLISH).contains(search))

                                ||

                                (project.getShortDescription() != null &&
                                        project.getShortDescription().toLowerCase(Locale.ENGLISH).contains(search))

                )

                .toList();

    }

    // =====================================
    // PAGINATION
    // =====================================

    public Page<Project> getProjects(int page) {

        Pageable pageable = PageRequest.of(

                Math.max(page - 1, 0),

                10,

                Sort.by("displayOrder")
                        .ascending()

        );

        return repository.findAll(pageable);

    }

    // =====================================
    // TOTAL PROJECTS
    // =====================================

    public long totalProjects() {

        return repository.count();

    }

    // =====================================
    // TOTAL PUBLISHED
    // =====================================

    public long totalPublished() {

        return repository.countByPublishedTrue();

    }

    // =====================================
    // TOTAL FEATURED
    // =====================================

    public long totalFeatured() {

        return repository.countByFeaturedTrue();

    }

    // =====================================
    // TOTAL DRAFT
    // =====================================

    public long totalDraft() {

        return repository.count()

                -

                repository.countByPublishedTrue();

    }

    // =====================================
    // TOTAL COMING SOON
    // =====================================

    public long totalComingSoon() {

        return repository.findAll()

                .stream()

                .filter(project ->

                        project.getProjectStatus() != null

                                &&

                                project.getProjectStatus()

                                        .equalsIgnoreCase("Coming Soon")

                )

                .count();

    }

    // =====================================
    // TOTAL LIVE PROJECTS
    // =====================================

    public long totalLiveProjects() {

        return repository.findAll()

                .stream()

                .filter(Project::isPublished)

                .count();

    }

    // =====================================
    // RECENT PROJECTS
    // =====================================

    public List<Project> recentProjects() {

        return repository.findAll(

                Sort.by(

                        Sort.Direction.DESC,

                        "createdAt"

                )

        );

    }
    // =====================================
    // UPLOAD GALLERY
    // =====================================

    public void uploadGallery(

            Long id,

            MultipartFile[] images

    ) {

        Project project = getById(id);

        try {

            if (images != null) {

                if (images.length > 0 && !images[0].isEmpty()) {

                    project.setImage1(

                            uploadService.uploadImage(

                                    images[0],

                                    "gallery"

                            )

                    );

                }

                if (images.length > 1 && !images[1].isEmpty()) {

                    project.setImage2(

                            uploadService.uploadImage(

                                    images[1],

                                    "gallery"

                            )

                    );

                }

                if (images.length > 2 && !images[2].isEmpty()) {

                    project.setImage3(

                            uploadService.uploadImage(

                                    images[2],

                                    "gallery"

                            )

                    );

                }

                if (images.length > 3 && !images[3].isEmpty()) {

                    project.setImage4(

                            uploadService.uploadImage(

                                    images[3],

                                    "gallery"

                            )

                    );

                }

                if (images.length > 4 && !images[4].isEmpty()) {

                    project.setImage5(

                            uploadService.uploadImage(

                                    images[4],

                                    "gallery"

                            )

                    );

                }

            }

            repository.save(project);

        }

        catch (Exception e) {

            throw new RuntimeException(

                    "Gallery upload failed.",

                    e

            );

        }

    }

    // =====================================
    // GENERATE SLUG
    // =====================================

    private String generateSlug(String title) {

        if (title == null || title.isBlank()) {

            return UUID.randomUUID().toString();

        }

        return title

                .toLowerCase(Locale.ENGLISH)

                .trim()

                .replaceAll("[^a-z0-9\\s-]", "")

                .replaceAll("\\s+", "-")

                .replaceAll("-+", "-");

    }
    // =====================================
    // PUBLISH PROJECT
    // =====================================

    public void publish(Long id) {

        Project project = getById(id);

        project.setPublished(true);

        repository.save(project);

    }

    // =====================================
    // UNPUBLISH PROJECT
    // =====================================

    public void unpublish(Long id) {

        Project project = getById(id);

        project.setPublished(false);

        repository.save(project);

    }

    // =====================================
    // FEATURE PROJECT
    // =====================================

    public void feature(Long id) {

        Project project = getById(id);

        project.setFeatured(true);

        repository.save(project);

    }

    // =====================================
    // REMOVE FEATURE
    // =====================================

    public void removeFeature(Long id) {

        Project project = getById(id);

        project.setFeatured(false);

        repository.save(project);

    }

    // =====================================
    // CHANGE DISPLAY ORDER
    // =====================================

    public void changeDisplayOrder(

            Long id,

            Integer displayOrder

    ) {

        Project project = getById(id);

        project.setDisplayOrder(displayOrder);

        repository.save(project);

    }

    // =====================================
    // TOGGLE PUBLISH
    // =====================================

    public void togglePublish(Long id) {

        Project project = getById(id);

        project.setPublished(

                !project.isPublished()

        );

        repository.save(project);

    }

    // =====================================
    // TOGGLE FEATURE
    // =====================================

    public void toggleFeature(Long id) {

        Project project = getById(id);

        project.setFeatured(

                !project.isFeatured()

        );

        repository.save(project);

    }

    // =====================================
    // EXISTS
    // =====================================

    public boolean exists(Long id) {

        return repository.existsById(id);

    }

    // =====================================
    // TOTAL FEATURED & PUBLISHED
    // =====================================

    public long totalFeaturedPublished() {

        return repository.findAll()

                .stream()

                .filter(Project::isFeatured)

                .filter(Project::isPublished)

                .count();

    }

    // =====================================
    // REFRESH SLUG
    // =====================================

    public void refreshSlug(Long id) {

        Project project = getById(id);

        project.setSlug(

                generateSlug(

                        project.getTitle()

                )

        );

        repository.save(project);

    }

}