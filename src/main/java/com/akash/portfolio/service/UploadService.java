package com.akash.portfolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/*
 * Stores uploaded files under ${file.upload-dir} and returns
 * the path relative to it (e.g. "projects/abc.png"), which the
 * templates serve as /uploads/{path}.
 */
@Service
public class UploadService {

    private static final Set<String> IMAGE_TYPES =
            Set.of("jpg", "jpeg", "png", "gif", "webp", "svg");

    private static final Set<String> VIDEO_TYPES =
            Set.of("mp4", "webm", "ogg", "mov");

    private static final Set<String> DOCUMENT_TYPES =
            Set.of("pdf");

    private final Path root;

    public UploadService(@Value("${file.upload-dir}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String uploadImage(MultipartFile file, String folder) throws IOException {
        return store(file, folder, IMAGE_TYPES);
    }

    public String uploadVideo(MultipartFile file) throws IOException {
        return store(file, "videos", VIDEO_TYPES);
    }

    public String uploadResume(MultipartFile file) throws IOException {
        return store(file, "resume", DOCUMENT_TYPES);
    }

    public void delete(String relativePath) throws IOException {

        Path target = resolveInsideRoot(relativePath);

        Files.deleteIfExists(target);

    }

    private String store(MultipartFile file,
                         String folder,
                         Set<String> allowedTypes) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IOException("File is empty.");
        }

        String original = StringUtils.cleanPath(
                file.getOriginalFilename() == null ? "" : file.getOriginalFilename());

        String extension = StringUtils.getFilenameExtension(original);

        if (extension == null
                || !allowedTypes.contains(extension.toLowerCase(Locale.ROOT))) {
            throw new IOException("Unsupported file type: " + original);
        }

        String fileName = UUID.randomUUID() + "." + extension.toLowerCase(Locale.ROOT);

        String relativePath = folder + "/" + fileName;

        Path target = resolveInsideRoot(relativePath);

        Files.createDirectories(target.getParent());

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        return relativePath;

    }

    private Path resolveInsideRoot(String relativePath) throws IOException {

        Path target = root.resolve(relativePath).normalize();

        if (!target.startsWith(root)) {
            throw new IOException("Invalid file path.");
        }

        return target;

    }

}
