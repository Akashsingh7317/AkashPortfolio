package com.akash.portfolio.service;

import com.akash.portfolio.entity.Experience;
import com.akash.portfolio.repository.ExperienceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceService {

    private final ExperienceRepository repository;

    public ExperienceService(ExperienceRepository repository) {
        this.repository = repository;
    }

    /*
     * Create Experience
     */
    public Experience createExperience(Experience experience) {

        return repository.save(experience);

    }

    /*
     * Update Experience
     */
    public Experience updateExperience(Long id, Experience request) {

        Experience experience = getExperienceById(id);

        experience.setCompanyName(request.getCompanyName());
        experience.setCompanyLogo(request.getCompanyLogo());
        experience.setDesignation(request.getDesignation());
        experience.setEmploymentType(request.getEmploymentType());
        experience.setLocation(request.getLocation());
        experience.setCompanyWebsite(request.getCompanyWebsite());
        experience.setStartDate(request.getStartDate());
        experience.setEndDate(request.getEndDate());
        experience.setCurrentCompany(request.isCurrentCompany());
        experience.setTechnologies(request.getTechnologies());
        experience.setResponsibilities(request.getResponsibilities());
        experience.setDisplayOrder(request.getDisplayOrder());
        experience.setFeatured(request.isFeatured());
        experience.setActive(request.isActive());

        return repository.save(experience);

    }

    /*
     * Delete Experience
     */
    public void deleteExperience(Long id) {

        Experience experience = getExperienceById(id);

        repository.delete(experience);

    }

    /*
     * Find By ID
     */
    public Experience getExperienceById(Long id) {

        return repository.findById(id)

                .orElseThrow(() ->
                        new RuntimeException("Experience not found."));

    }

    /*
     * Get All Experience
     */
    public List<Experience> getAllExperience() {

        return repository.findAll();

    }

    /*
     * Active Experience
     */
    public List<Experience> getActiveExperience() {

        return repository.findByActiveTrueOrderByStartDateDesc();

    }

    /*
     * Current Company
     */
    public Experience getCurrentCompany() {

        return repository.findByActiveTrueOrderByStartDateDesc()

                .stream()

                .filter(Experience::isCurrentCompany)

                .findFirst()

                .orElse(null);

    }

    /*
     * Featured Experience
     */
    public List<Experience> getFeaturedExperience() {

        return repository.findByActiveTrueOrderByStartDateDesc()

                .stream()

                .filter(Experience::isFeatured)

                .toList();

    }

    /*
     * Activate Experience
     */
    public void activateExperience(Long id) {

        Experience experience = getExperienceById(id);

        experience.setActive(true);

        repository.save(experience);

    }

    /*
     * Deactivate Experience
     */
    public void deactivateExperience(Long id) {

        Experience experience = getExperienceById(id);

        experience.setActive(false);

        repository.save(experience);

    }

    /*
     * Feature Experience
     */
    public void featureExperience(Long id) {

        Experience experience = getExperienceById(id);

        experience.setFeatured(true);

        repository.save(experience);

    }

    /*
     * Remove Featured
     */
    public void removeFeatured(Long id) {

        Experience experience = getExperienceById(id);

        experience.setFeatured(false);

        repository.save(experience);

    }

    /*
     * Dashboard Count
     */
    public long totalExperience() {

        return repository.count();

    }

    /*
     * Active Count
     */
    public long activeExperienceCount() {

        return repository.countByActiveTrue();

    }

}