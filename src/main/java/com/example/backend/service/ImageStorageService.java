
package com.example.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {

    private final Path uploadDirectory =
            Paths.get("uploads").toAbsolutePath().normalize();

    public String saveImage(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select an image.");
        }

        Set<String> allowedTypes = Set.of(
                "image/jpeg",
                "image/png",
                "image/webp"
        );

        if (!allowedTypes.contains(file.getContentType())) {
            throw new IllegalArgumentException(
                    "Only JPG, PNG, and WebP images are allowed."
            );
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "Image must be 10 MB or smaller."
            );
        }

        Files.createDirectories(uploadDirectory);

        String originalName = StringUtils.cleanPath(
                file.getOriginalFilename() == null
                        ? "artwork"
                        : file.getOriginalFilename()
        );

        String extension = "";

        int dotIndex = originalName.lastIndexOf(".");
        if (dotIndex >= 0) {
            extension = originalName.substring(dotIndex).toLowerCase();
        }

        String filename = UUID.randomUUID() + extension;

        Path destination = uploadDirectory.resolve(filename).normalize();

        if (!destination.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException("Invalid filename.");
        }

        Files.copy(
                file.getInputStream(),
                destination,
                StandardCopyOption.REPLACE_EXISTING
        );

        return "/uploads/" + filename;
    }
}
