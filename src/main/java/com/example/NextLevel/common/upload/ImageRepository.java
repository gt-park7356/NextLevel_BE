package com.example.NextLevel.common.upload;

import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository {
    String upload(MultipartFile file);
}
