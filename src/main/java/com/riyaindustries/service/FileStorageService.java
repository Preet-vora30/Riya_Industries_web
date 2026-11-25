package com.riyaindustries.service;

import com.riyaindustries.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_DRAWING_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp", "pdf", "dxf", "dwg");
    private static final long MAX_FILE_BYTES = 10L * 1024L * 1024L;

    private final Path rootLocation;

    public FileStorageService(@Value("${app.upload-dir:./uploads}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public StoredFile storePublicImage(MultipartFile file, String subfolder) {
        return store(file, Paths.get("public", subfolder), ALLOWED_IMAGE_EXTENSIONS, true);
    }

    public StoredFile storeQuotationDrawing(MultipartFile file) {
        return store(file, Paths.get("drawings"), ALLOWED_DRAWING_EXTENSIONS, false);
    }

    public Resource loadAsResource(String relativeStoragePath) {
        try {
            Path file = rootLocation.resolve(relativeStoragePath).normalize();
            if (!file.startsWith(rootLocation)) {
                throw new StorageException("Invalid file path.");
            }
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new StorageException("File not found.");
        } catch (MalformedURLException e) {
            throw new StorageException("Could not read file.", e);
        }
    }

    private StoredFile store(MultipartFile file, Path subPath, Set<String> allowedExtensions, boolean publicUrl) {
        if (file == null || file.isEmpty()) {
            throw new StorageException("Uploaded file is empty.");
        }
        if (file.getSize() > MAX_FILE_BYTES) {
            throw new StorageException("File exceeds maximum allowed size of 10 MB.");
        }

        String originalName = StringUtils.cleanPath(
            file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"
        );
        String extension = extractExtension(originalName);
        if (!allowedExtensions.contains(extension)) {
            throw new StorageException("Unsupported file type '." + extension + "'. Allowed: " + String.join(", ", allowedExtensions));
        }

        String storedName = UUID.randomUUID() + "." + extension;
        Path targetDir = rootLocation.resolve(subPath).normalize();
        if (!targetDir.startsWith(rootLocation)) {
            throw new StorageException("Invalid storage directory.");
        }

        try {
            Files.createDirectories(targetDir);
            Path destination = targetDir.resolve(storedName).normalize();
            if (!destination.startsWith(targetDir)) {
                throw new StorageException("Invalid destination path.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store uploaded file.", e);
        }

        String relativePath = subPath.resolve(storedName).toString().replace("\\", "/");
        String url = publicUrl ? "/uploads/" + relativePath : null;
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        return new StoredFile(originalName, storedName, contentType, file.getSize(), relativePath, url);
    }

    private String extractExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx < 0 || idx == filename.length() - 1) {
            return "";
        }
        return filename.substring(idx + 1).toLowerCase(Locale.ROOT);
    }

    public record StoredFile(
        String originalFilename,
        String storedFilename,
        String contentType,
        long fileSizeBytes,
        String relativePath,
        String publicUrl
    ) {
    }
}
