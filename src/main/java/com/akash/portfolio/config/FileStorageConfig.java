package com.akash.portfolio.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileStorageConfig {

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    @PostConstruct
    public void init() {

        try {

            createDirectory(uploadDirectory);

            createDirectory(uploadDirectory + "/profile");

            createDirectory(uploadDirectory + "/projects");

            createDirectory(uploadDirectory + "/videos");

            createDirectory(uploadDirectory + "/certificates");

            createDirectory(uploadDirectory + "/resume");

            createDirectory(uploadDirectory + "/hero");

            createDirectory(uploadDirectory + "/gallery");

        } catch (IOException e) {

            throw new RuntimeException("Unable to create upload directories.", e);

        }

    }

    private void createDirectory(String directory) throws IOException {

        Path path = Paths.get(directory);

        if (!Files.exists(path)) {

            Files.createDirectories(path);

        }

    }

}