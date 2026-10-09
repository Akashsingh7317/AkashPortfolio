package com.akash.portfolio.service;

import com.akash.portfolio.entity.Skill;
import com.akash.portfolio.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository repository;

    public SkillService(SkillRepository repository) {
        this.repository = repository;
    }

    /*
     * Add Skill
     */
    public Skill createSkill(Skill skill) {

        return repository.save(skill);

    }

    /*
     * Update Skill
     */
    public Skill updateSkill(Long id, Skill request) {

        Skill skill = getSkillById(id);

        skill.setSkillName(request.getSkillName());

        skill.setCategory(request.getCategory());

        skill.setIcon(request.getIcon());

        skill.setPercentage(request.getPercentage());

        skill.setDisplayOrder(request.getDisplayOrder());

        skill.setFeatured(request.isFeatured());

        skill.setActive(request.isActive());

        skill.setDescription(request.getDescription());

        return repository.save(skill);

    }

    /*
     * Delete Skill
     */
    public void deleteSkill(Long id) {

        Skill skill = getSkillById(id);

        repository.delete(skill);

    }

    /*
     * Find Skill By ID
     */
    public Skill getSkillById(Long id) {

        return repository.findById(id)

                .orElseThrow(() ->
                        new RuntimeException("Skill not found."));

    }

    /*
     * Get All Skills
     */
    public List<Skill> getAllSkills() {

        return repository.findAll();

    }

    /*
     * Active Skills
     */
    public List<Skill> getActiveSkills() {

        return repository.findByActiveTrueOrderByDisplayOrderAsc();

    }

    /*
     * Featured Skills
     */
    public List<Skill> getFeaturedSkills() {

        return repository.findByActiveTrueOrderByDisplayOrderAsc()

                .stream()

                .filter(Skill::isFeatured)

                .toList();

    }

    /*
     * Category Wise Skills
     */
    public List<Skill> getSkillsByCategory(String category) {

        return repository.findByCategoryIgnoreCase(category);

    }

    /*
     * Activate Skill
     */
    public void activateSkill(Long id) {

        Skill skill = getSkillById(id);

        skill.setActive(true);

        repository.save(skill);

    }

    /*
     * Deactivate Skill
     */
    public void deactivateSkill(Long id) {

        Skill skill = getSkillById(id);

        skill.setActive(false);

        repository.save(skill);

    }

    /*
     * Feature Skill
     */
    public void featureSkill(Long id) {

        Skill skill = getSkillById(id);

        skill.setFeatured(true);

        repository.save(skill);

    }

    /*
     * Remove Featured
     */
    public void removeFeatured(Long id) {

        Skill skill = getSkillById(id);

        skill.setFeatured(false);

        repository.save(skill);

    }

    /*
     * Dashboard Count
     */
    public long totalSkills() {

        return repository.count();

    }

    /*
     * Active Count
     */
    public long activeSkills() {

        return repository.countByActiveTrue();

    }

}