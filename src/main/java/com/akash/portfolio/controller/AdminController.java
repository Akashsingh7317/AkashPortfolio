package com.akash.portfolio.controller;

import com.akash.portfolio.dto.LoginDTO;
import com.akash.portfolio.entity.Admin;
import com.akash.portfolio.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /*
     * Login Page
     */
    @GetMapping("/login")
    public String loginPage(Model model) {

        model.addAttribute("loginDTO", new LoginDTO());

        return "admin/login";

    }

    /*
     * Dashboard Redirect
     */
    @GetMapping("")
    public String adminHome() {

        return "redirect:/admin/dashboard";

    }

    /*
     * Admin Profile
     */
    @GetMapping("/profile")
    public String profile(Authentication authentication,
                          Model model) {

        if (authentication != null) {

            Admin admin = adminService.getByUsername(
                    authentication.getName());

            model.addAttribute("admin", admin);

        }

        return "admin/profile";

    }

    /*
     * Update Profile
     */
    @PostMapping("/profile")
    public String updateProfile(

            @ModelAttribute Admin admin

    ) {

        adminService.updateAdmin(
                admin.getId(),
                admin
        );

        return "redirect:/admin/profile?updated";

    }

    /*
     * Change Password Page
     */
    @GetMapping("/change-password")
    public String changePasswordPage() {

        return "admin/change-password";

    }

    /*
     * Change Password
     */
    @PostMapping("/change-password")
    public String changePassword(

            @RequestParam String currentPassword,

            @RequestParam String newPassword,

            Authentication authentication

    ) {

        Admin admin =
                adminService.getByUsername(
                        authentication.getName());

        admin.setPassword(newPassword);

        adminService.updateAdmin(
                admin.getId(),
                admin);

        return "redirect:/admin/profile?passwordChanged";

    }

    /*
     * Logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/admin/login?logout";

    }

}