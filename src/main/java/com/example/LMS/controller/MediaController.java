package com.example.LMS.controller;

import com.example.LMS.dto.SuccessResponse;
import com.example.LMS.service.MediaService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequestMapping("/media")
@RestController
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class MediaController {
    MediaService mediaService;

    @PostMapping("/upload")
    public SuccessResponse<Map> uploadMedia(@RequestParam("file") MultipartFile file) throws Exception {
        return new SuccessResponse<>(HttpStatus.OK.value(), "Media uploaded successfully", mediaService.uploadMedia(file));
    }

    @DeleteMapping("/delete/{publicId}")
    public SuccessResponse<?> deleteMedia(@NotBlank @PathVariable String publicId) throws Exception {
        mediaService.deleteMedia(publicId);
        return new SuccessResponse<>(HttpStatus.OK.value(), "Deleted successful");
    }
}
