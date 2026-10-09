package com.akash.portfolio.controller;

import com.akash.portfolio.entity.Experience;
import com.akash.portfolio.service.ExperienceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    /*
     * Experience List
     */
    @GetMapping
    public String listExperience(Model model) {

        model.addAttribute(
                "experiences",
                experienceService.getAllExperience());

        return "admin/experience/list";
    }

    /*
     * Add Experience Page
     */
    @GetMapping("/add")
    public String addExperience(Model model) {

        model.addAttribute(
                "experience",
                new Experience());

        return "admin/experience/add";
    }

    /*
     * Save Experience
     */
    @PostMapping("/save")
    public String saveExperience(
            @ModelAttribute Experience experience) {

        experienceService.createExperience(experience);

        return "redirect:/admin/experience?success";
    }

    /*
     * Edit Experience
     */
    @GetMapping("/edit/{id}")
    public String editExperience(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "experience",
                experienceService.getExperienceById(id));

        return "admin/experience/edit";
    }

    /*
     * Update Experience
     */
    @PostMapping("/update/{id}")
    public String updateExperience(
            @PathVariable Long id,
            @ModelAttribute Experience experience) {

        experienceService.updateExperience(id, experience);

        return "redirect:/admin/experience?updated";
    }

    /*
     * View Experience
     */
    @GetMapping("/view/{id}")
    public String viewExperience(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "experience",
                experienceService.getExperienceById(id));

        return "admin/experience/view";
    }

    /*
     * Delete Experience
     */
    @GetMapping("/delete/{id}")
    public String deleteExperience(
            @PathVariable Long id) {

        experienceService.deleteExperience(id);

        return "redirect:/admin/experience?deleted";
    }

    /*
     * Activate Experience
     */
    @GetMapping("/activate/{id}")
    public String activateExperience(
            @PathVariable Long id) {

        experienceService.activateExperience(id);

        return "redirect:/admin/experience";
    }

    /*
     * Deactivate Experience
     */
    @GetMapping("/deactivate/{id}")
    public String deactivateExperience(
            @PathVariable Long id) {

        experienceService.deactivateExperience(id);

        return "redirect:/admin/experience";
    }

    /*
     * Feature Experience
     */
    @GetMapping("/feature/{id}")
    public String featureExperience(
            @PathVariable Long id) {

        experienceService.featureExperience(id);

        return "redirect:/admin/experience";
    }

    /*
     * Remove Feature
     */
    @GetMapping("/remove-feature/{id}")
    public String removeFeature(
            @PathVariable Long id) {

        experienceService.removeFeatured(id);

        return "redirect:/admin/experience";
    }

    /*
     * Current Company
     */
    @GetMapping("/current")
    public String currentCompany(Model model) {

        model.addAttribute(
                "experience",
                experienceService.getCurrentCompany());

        return "admin/experience/current";
    }

    /*
     * Experience Statistics
     */
    @GetMapping("/statistics")
    public String statistics(Model model) {

        model.addAttribute(
                "totalExperience",
                experienceService.totalExperience());

        model.addAttribute(
                "activeExperience",
                experienceService.activeExperienceCount());

        return "admin/experience/statistics";
    }

}