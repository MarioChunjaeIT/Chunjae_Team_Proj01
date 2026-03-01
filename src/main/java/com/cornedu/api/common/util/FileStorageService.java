package com.cornedu.api.common.util;

import com.cornedu.api.common.exception.BusinessException;
import com.cornedu.api.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService(@Value("${app.upload-dir:./uploads}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            log.warn("Upload directory creation failed: {}", e.getMessage());
        }
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) return null;
        String ext = "";
        int i = originalName.lastIndexOf('.');
        if (i > 0) ext = originalName.substring(i);
        String filename = UUID.randomUUID().toString() + ext;
        try {
            Path target = rootLocation.resolve(filename);
            Files.copy(file.getInputStream(), target);
            return filename;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }
}
