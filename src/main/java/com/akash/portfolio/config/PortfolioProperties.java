package com.akash.portfolio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
 * Public profile details shown on the portfolio.
 * Values come from the "portfolio.*" keys in application.properties.
 */
@Component
@ConfigurationProperties(prefix = "portfolio")
public class PortfolioProperties {

    private String name = "Akash Singh";
    private String role = "Java Full Stack Developer";
    private String email;
    private String phone;
    private String location;
    private String githubUsername;
    private String linkedinUrl;
    private String leetcodeUrl;
    private String avatarUrl;
    private String resumeFile = "resume/Akash_Resume.pdf";

    public String getGithubUrl() {
        return githubUsername == null || githubUsername.isBlank()
                ? null
                : "https://github.com/" + githubUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getLeetcodeUrl() {
        return leetcodeUrl;
    }

    public void setLeetcodeUrl(String leetcodeUrl) {
        this.leetcodeUrl = leetcodeUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getResumeFile() {
        return resumeFile;
    }

    public void setResumeFile(String resumeFile) {
        this.resumeFile = resumeFile;
    }
}
