package com.AGETIC.Civil_service_competition.service.impl;

import java.io.IOException;
import java.nio.file.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.AGETIC.Civil_service_competition.service.FileStorageService;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${storage.base-dir:uploads}")
    private String baseDir;

    @Override
    public String store(MultipartFile file, String directory) throws IOException {
        if (file == null || file.isEmpty()) throw new IOException("Empty file");
        Path root = Paths.get(baseDir).toAbsolutePath().normalize();
        Path dir = root.resolve(directory).normalize();
        Files.createDirectories(dir);

        String original = Path.of(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename())
                              .getFileName().toString();
        String safe = original.replaceAll("[^a-zA-Z0-9._-]", "_");
        Path target = dir.resolve(System.currentTimeMillis() + "_" + safe);
        file.transferTo(target);

        // return a filesystem path (for dev). Later you can return a public URL if using a server/CDN.
        return target.toString();
    }
}
