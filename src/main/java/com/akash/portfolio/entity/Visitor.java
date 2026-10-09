package com.akash.portfolio.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitors")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String sessionId;

    @Column(length = 100)
    private String ipAddress;

    @Column(length = 500)
    private String userAgent;

    @Column(length = 100)
    private String browser;

    @Column(length = 100)
    private String operatingSystem;

    @Column(length = 50)
    private String deviceType;

    @Column(length = 150)
    private String country;

    @Column(length = 150)
    private String state;

    @Column(length = 150)
    private String city;

    @Column(length = 255)
    private String referrer;

    @Column(length = 255)
    private String visitedPage;

    @Column(nullable = false)
    private Integer totalVisits = 1;

    @Column(nullable = false)
    private LocalDateTime firstVisit;

    @Column(nullable = false)
    private LocalDateTime lastVisit;

    @Column(nullable = false)
    private boolean activeSession = true;

    public Visitor() {
    }

    @PrePersist
    public void prePersist() {

        firstVisit = LocalDateTime.now();
        lastVisit = LocalDateTime.now();

    }

    @PreUpdate
    public void preUpdate() {

        lastVisit = LocalDateTime.now();

    }

    // ===========================
    // Getters & Setters
    // ===========================

    public Long getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getVisitedPage() {
        return visitedPage;
    }

    public void setVisitedPage(String visitedPage) {
        this.visitedPage = visitedPage;
    }

    public Integer getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(Integer totalVisits) {
        this.totalVisits = totalVisits;
    }

    public LocalDateTime getFirstVisit() {
        return firstVisit;
    }

    public LocalDateTime getLastVisit() {
        return lastVisit;
    }

    public boolean isActiveSession() {
        return activeSession;
    }

    public void setActiveSession(boolean activeSession) {
        this.activeSession = activeSession;
    }

}