package com.example.LMS.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MediaService implements IMediaService {
    final Cloudinary cloudinary;

    public Map uploadMedia(MultipartFile file) throws IOException {
        try {
            String contentType = file.getContentType();
            Map params = ObjectUtils.asMap(
                    "folder", ""
            );
            if (contentType == null || contentType.startsWith("video/"))
                params.put("resource_type", "video");
            else
                params.put("resource_type", "image");
            return cloudinary.uploader().upload(file.getBytes(), params);
        } catch (IOException e) {
            throw new IOException("Failed to upload media");
        }
    }

    public void deleteMedia(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
