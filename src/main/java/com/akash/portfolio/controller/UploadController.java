package com.akash.portfolio.controller;

import com.akash.portfolio.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/upload")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    /*
     * Upload Profile Image
     */
    @PostMapping("/profile")
    @ResponseBody
    public ResponseEntity<String> uploadProfile(

            @RequestParam("file")
            MultipartFile file

    ) {

        try {

            String path =
                    uploadService.uploadImage(
                            file,
                            "profile");

            return ResponseEntity.ok(path);

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        }

    }

    /*
     * Upload Project Thumbnail
     */
    @PostMapping("/project-thumbnail")
    @ResponseBody
    public ResponseEntity<String> uploadThumbnail(

            @RequestParam("file")
            MultipartFile file

    ) {

        try {

            String path =
                    uploadService.uploadImage(
                            file,
                            "projects");

            return ResponseEntity.ok(path);

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        }

    }

    /*
     * Upload Gallery Image
     */
    @PostMapping("/gallery")
    @ResponseBody
    public ResponseEntity<String> uploadGallery(

            @RequestParam("file")
            MultipartFile file

    ) {

        try {

            String path =
                    uploadService.uploadImage(
                            file,
                            "gallery");

            return ResponseEntity.ok(path);

        }

        catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        }

    }

    /*
     * Upload Certificate Image
     */
    @PostMapping("/certificate")
    @ResponseBody
    public ResponseEntity<String> uploadCertificate(

            @RequestParam("file")
            MultipartFile file

    ) {

        try {

            String path =
                    uploadService.uploadImage(
                            file,
                            "certificates");

            return ResponseEntity.ok(path);

        }

        catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        }

    }

    /*
     * Upload Resume
     */
    @PostMapping("/resume")
    @ResponseBody
    public ResponseEntity<String> uploadResume(

            @RequestParam("file")
            MultipartFile file

    ) {

        try {

            String path =
                    uploadService.uploadResume(file);

            return ResponseEntity.ok(path);

        }

        catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        }

    }

    /*
     * Upload Demo Video
     */
    @PostMapping("/video")
    @ResponseBody
    public ResponseEntity<String> uploadVideo(

            @RequestParam("file")
            MultipartFile file

    ) {

        try {

            String path =
                    uploadService.uploadVideo(file);

            return ResponseEntity.ok(path);

        }

        catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        }

    }

    /*
     * Delete Uploaded File
     */
    @DeleteMapping
    @ResponseBody
    public ResponseEntity<String> deleteFile(

            @RequestParam String path

    ) {

        try {

            uploadService.delete(path);

            return ResponseEntity.ok(
                    "File deleted successfully.");

        }

        catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        }

    }

}