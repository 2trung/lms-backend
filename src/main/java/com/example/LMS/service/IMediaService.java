package com.example.LMS.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface IMediaService {
    Map uploadMedia(MultipartFile file) throws IOException;
    void deleteMedia(String publicId) throws IOException;
}
