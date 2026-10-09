package com.akash.portfolio.service;

import com.akash.portfolio.entity.Certificate;
import com.akash.portfolio.repository.CertificateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService {

    private final CertificateRepository repository;

    public CertificateService(CertificateRepository repository) {
        this.repository = repository;
    }

    /*
     * Create Certificate
     */
    public Certificate createCertificate(Certificate certificate) {

        return repository.save(certificate);

    }

    /*
     * Update Certificate
     */
    public Certificate updateCertificate(Long id, Certificate request) {

        Certificate certificate = getCertificateById(id);

        certificate.setTitle(request.getTitle());
        certificate.setIssuer(request.getIssuer());
        certificate.setCredentialId(request.getCredentialId());
        certificate.setCredentialUrl(request.getCredentialUrl());
        certificate.setCertificateImage(request.getCertificateImage());
        certificate.setCertificatePdf(request.getCertificatePdf());
        certificate.setIssueDate(request.getIssueDate());
        certificate.setExpiryDate(request.getExpiryDate());
        certificate.setSkills(request.getSkills());
        certificate.setDescription(request.getDescription());
        certificate.setFeatured(request.isFeatured());
        certificate.setActive(request.isActive());
        certificate.setDisplayOrder(request.getDisplayOrder());

        return repository.save(certificate);

    }

    /*
     * Delete Certificate
     */
    public void deleteCertificate(Long id) {

        Certificate certificate = getCertificateById(id);

        repository.delete(certificate);

    }

    /*
     * Find By ID
     */
    public Certificate getCertificateById(Long id) {

        return repository.findById(id)

                .orElseThrow(() ->
                        new RuntimeException("Certificate not found."));

    }

    /*
     * Get All Certificates
     */
    public List<Certificate> getAllCertificates() {

        return repository.findAll();

    }

    /*
     * Active Certificates
     */
    public List<Certificate> getActiveCertificates() {

        return repository.findByActiveTrueOrderByDisplayOrderAsc();

    }

    /*
     * Featured Certificates
     */
    public List<Certificate> getFeaturedCertificates() {

        return repository.findByFeaturedTrueOrderByDisplayOrderAsc();

    }

    /*
     * Activate Certificate
     */
    public void activateCertificate(Long id) {

        Certificate certificate = getCertificateById(id);

        certificate.setActive(true);

        repository.save(certificate);

    }

    /*
     * Deactivate Certificate
     */
    public void deactivateCertificate(Long id) {

        Certificate certificate = getCertificateById(id);

        certificate.setActive(false);

        repository.save(certificate);

    }

    /*
     * Feature Certificate
     */
    public void featureCertificate(Long id) {

        Certificate certificate = getCertificateById(id);

        certificate.setFeatured(true);

        repository.save(certificate);

    }

    /*
     * Remove Featured
     */
    public void removeFeatured(Long id) {

        Certificate certificate = getCertificateById(id);

        certificate.setFeatured(false);

        repository.save(certificate);

    }

    /*
     * Dashboard Count
     */
    public long totalCertificates() {

        return repository.count();

    }

    /*
     * Active Count
     */
    public long activeCertificates() {

        return repository.countByActiveTrue();

    }

}