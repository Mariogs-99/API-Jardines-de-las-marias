package com.hotelJB.hotelJB_API.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageService {
    String saveFile(MultipartFile file);
    Resource loadFile(String fileName);
    Path getStoragePath();
}
