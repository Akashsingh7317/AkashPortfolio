package com.akash.portfolio.controller;

import com.akash.portfolio.entity.Skill;
import com.akash.portfolio.service.SkillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    /*
     * Skill List
     */
    @GetMapping
    public String listSkills(Model model) {

        model.addAttribute(
                "skills",
                skillService.getAllSkills());

        return "admin/skill/list";

    }

    /*
     * Add Skill Page
     */
    @GetMapping("/add")
    public String addSkillPage(Model model) {

        model.addAttribute(
                "skill",
                new Skill());

        return "admin/skill/add";

    }

    /*
     * Save Skill
     */
    @PostMapping("/save")
    public String saveSkill(
            @ModelAttribute Skill skill) {

        skillService.createSkill(skill);

        return "redirect:/admin/skills?success";

    }

    /*
     * Edit Skill
     */
    @GetMapping("/edit/{id}")
    public String editSkill(

            @PathVariable Long id,

            Model model

    ) {

        model.addAttribute(
                "skill",
                skillService.getSkillById(id));

        return "admin/skill/edit";

    }

    /*
     * Update Skill
     */
    @PostMapping("/update/{id}")
    public String updateSkill(

            @PathVariable Long id,

            @ModelAttribute Skill skill

    ) {

        skillService.updateSkill(id, skill);

        return "redirect:/admin/skills?updated";

    }

    /*
     * View Skill
     */
    @GetMapping("/view/{id}")
    public String viewSkill(

            @PathVariable Long id,

            Model model

    ) {

        model.addAttribute(
                "skill",
                skillService.getSkillById(id));

        return "admin/skill/view";

    }

    /*
     * Delete Skill
     */
    @GetMapping("/delete/{id}")
    public String deleteSkill(
            @PathVariable Long id) {

        skillService.deleteSkill(id);

        return "redirect:/admin/skills?deleted";

    }

    /*
     * Activate Skill
     */
    @GetMapping("/activate/{id}")
    public String activateSkill(
            @PathVariable Long id) {

        skillService.activateSkill(id);

        return "redirect:/admin/skills";

    }

    /*
     * Deactivate Skill
     */
    @GetMapping("/deactivate/{id}")
    public String deactivateSkill(
            @PathVariable Long id) {

        skillService.deactivateSkill(id);

        return "redirect:/admin/skills";

    }

    /*
     * Feature Skill
     */
    @GetMapping("/feature/{id}")
    public String featureSkill(
            @PathVariable Long id) {

        skillService.featureSkill(id);

        return "redirect:/admin/skills";

    }

    /*
     * Remove Feature
     */
    @GetMapping("/remove-feature/{id}")
    public String removeFeature(
            @PathVariable Long id) {

        skillService.removeFeatured(id);

        return "redirect:/admin/skills";

    }

    /*
     * Skill Statistics
     */
    @GetMapping("/statistics")
    public String statistics(Model model) {

        model.addAttribute(
                "totalSkills",
                skillService.totalSkills());

        model.addAttribute(
                "activeSkills",
                skillService.activeSkills());

        return "admin/skill/statistics";

    }

}