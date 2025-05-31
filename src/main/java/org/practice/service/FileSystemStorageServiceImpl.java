package org.practice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
@PropertySource("classpath:application.properties")
@Slf4j
public class FileSystemStorageServiceImpl implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageServiceImpl(@Value("${image.location}") String location) {
        this.rootLocation = Paths.get(location);
    }

    @Override
    public void store(MultipartFile file, Long postId) {
        try {
            Path destinationFile = rootLocation.resolve(Paths.get(postId.toString())).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }
            File destFile = new File(rootLocation.toFile(), postId.toString());

            try (InputStream input = file.getInputStream()) {
                Files.copy(input, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Files.delete(rootLocation.resolve(filename));
        } catch (NoSuchFileException e) {
            log.warn("Could not delete file {} because no such file in: {}", filename, rootLocation.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (FileAlreadyExistsException e) {
            log.warn("Could not create directory because directory already exists: {}", rootLocation.toAbsolutePath(), e);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @EventListener
    public void populate(ContextRefreshedEvent event) {
        init();
    }
}
