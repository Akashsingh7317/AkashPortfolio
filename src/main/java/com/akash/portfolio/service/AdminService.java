package com.akash.portfolio.service;

import com.akash.portfolio.entity.Admin;
import com.akash.portfolio.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository,
                        PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create Admin
     */
    public Admin createAdmin(Admin admin) {

        if (adminRepository.existsByUsername(admin.getUsername())) {
            throw new RuntimeException("Username already exists.");
        }

        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        return adminRepository.save(admin);

    }

    /**
     * Update Admin
     */
    public Admin updateAdmin(Long id, Admin updatedAdmin) {

        Admin admin = getAdminById(id);

        admin.setFullName(updatedAdmin.getFullName());

        admin.setUsername(updatedAdmin.getUsername());

        admin.setEmail(updatedAdmin.getEmail());

        if (updatedAdmin.getPassword() != null &&
                !updatedAdmin.getPassword().isBlank()) {

            admin.setPassword(
                    passwordEncoder.encode(updatedAdmin.getPassword())
            );

        }

        admin.setRole(updatedAdmin.getRole());

        admin.setEnabled(updatedAdmin.isEnabled());

        admin.setAccountNonExpired(
                updatedAdmin.isAccountNonExpired());

        admin.setAccountNonLocked(
                updatedAdmin.isAccountNonLocked());

        admin.setCredentialsNonExpired(
                updatedAdmin.isCredentialsNonExpired());

        return adminRepository.save(admin);

    }

    /**
     * Get Admin By ID
     */
    public Admin getAdminById(Long id) {

        return adminRepository.findById(id)

                .orElseThrow(() ->
                        new RuntimeException("Admin not found."));

    }

    /**
     * Get By Username
     */
    public Admin getByUsername(String username) {

        return adminRepository.findByUsername(username)

                .orElseThrow(() ->
                        new RuntimeException("Invalid username."));

    }

    /**
     * Get By Email
     */
    public Admin getByEmail(String email) {

        return adminRepository.findByEmail(email)

                .orElseThrow(() ->
                        new RuntimeException("Email not found."));

    }

    /**
     * Get All Admins
     */
    public List<Admin> getAllAdmins() {

        return adminRepository.findAll();

    }

    /**
     * Delete Admin
     */
    public void deleteAdmin(Long id) {

        Admin admin = getAdminById(id);

        adminRepository.delete(admin);

    }

    /**
     * Update Last Login
     */
    public void updateLastLogin(String username) {

        Admin admin = getByUsername(username);

        admin.setLastLogin(LocalDateTime.now());

        adminRepository.save(admin);

    }

    /**
     * Enable Admin
     */
    public void enableAdmin(Long id) {

        Admin admin = getAdminById(id);

        admin.setEnabled(true);

        adminRepository.save(admin);

    }

    /**
     * Disable Admin
     */
    public void disableAdmin(Long id) {

        Admin admin = getAdminById(id);

        admin.setEnabled(false);

        adminRepository.save(admin);

    }

    /**
     * Total Admin Count
     */
    public long totalAdmins() {

        return adminRepository.count();

    }

}