package com.akash.portfolio.controller;

import com.akash.portfolio.entity.Certificate;
import com.akash.portfolio.service.CertificateService;
import com.akash.portfolio.service.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/certificates")
public class CertificateController {

    private final CertificateService certificateService;
    private final UploadService uploadService;

    public CertificateController(CertificateService certificateService,
                                 UploadService uploadService) {
        this.certificateService = certificateService;
        this.uploadService = uploadService;
    }

    /*
     * Certificate List
     */
    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "certificates",
                certificateService.getAllCertificates());

        return "admin/certificate/list";

    }

    /*
     * Add Certificate Page
     */
    @GetMapping("/add")
    public String addPage(Model model) {

        model.addAttribute(
                "certificate",
                new Certificate());

        return "admin/certificate/add";

    }

    /*
     * Save Certificate
     */
    @PostMapping("/save")
    public String save(

            @ModelAttribute Certificate certificate,

            @RequestParam(value = "image", required = false)
            MultipartFile image,

            @RequestParam(value = "pdf", required = false)
            MultipartFile pdf

    ) {

        try {

            if (image != null && !image.isEmpty()) {

                String imagePath =
                        uploadService.uploadImage(
                                image,
                                "certificates");

                certificate.setCertificateImage(imagePath);

            }

            if (pdf != null && !pdf.isEmpty()) {

                String pdfPath =
                        uploadService.uploadResume(pdf);

                certificate.setCertificatePdf(pdfPath);

            }

            certificateService.createCertificate(certificate);

            return "redirect:/admin/certificates?success";

        }

        catch (Exception e) {

            return "redirect:/admin/certificates/add?error";

        }

    }

    /*
     * Edit Certificate
     */
    @GetMapping("/edit/{id}")
    public String edit(

            @PathVariable Long id,

            Model model

    ) {

        model.addAttribute(
                "certificate",
                certificateService.getCertificateById(id));

        return "admin/certificate/edit";

    }

    /*
     * Update Certificate
     */
    @PostMapping("/update/{id}")
    public String update(

            @PathVariable Long id,

            @ModelAttribute Certificate certificate

    ) {

        certificateService.updateCertificate(
                id,
                certificate);

        return "redirect:/admin/certificates?updated";

    }

    /*
     * View Certificate
     */
    @GetMapping("/view/{id}")
    public String view(

            @PathVariable Long id,

            Model model

    ) {

        model.addAttribute(
                "certificate",
                certificateService.getCertificateById(id));

        return "admin/certificate/view";

    }

    /*
     * Delete Certificate
     */
    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id) {

        certificateService.deleteCertificate(id);

        return "redirect:/admin/certificates?deleted";

    }

    /*
     * Activate Certificate
     */
    @GetMapping("/activate/{id}")
    public String activate(
            @PathVariable Long id) {

        certificateService.activateCertificate(id);

        return "redirect:/admin/certificates";

    }

    /*
     * Deactivate Certificate
     */
    @GetMapping("/deactivate/{id}")
    public String deactivate(
            @PathVariable Long id) {

        certificateService.deactivateCertificate(id);

        return "redirect:/admin/certificates";

    }

    /*
     * Feature Certificate
     */
    @GetMapping("/feature/{id}")
    public String feature(
            @PathVariable Long id) {

        certificateService.featureCertificate(id);

        return "redirect:/admin/certificates";

    }

    /*
     * Remove Featured
     */
    @GetMapping("/remove-feature/{id}")
    public String removeFeature(
            @PathVariable Long id) {

        certificateService.removeFeatured(id);

        return "redirect:/admin/certificates";

    }

    /*
     * Certificate Statistics
     */
    @GetMapping("/statistics")
    public String statistics(Model model) {

        model.addAttribute(
                "totalCertificates",
                certificateService.totalCertificates());

        model.addAttribute(
                "activeCertificates",
                certificateService.activeCertificates());

        return "admin/certificate/statistics";

    }

}