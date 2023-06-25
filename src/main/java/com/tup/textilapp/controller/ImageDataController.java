package com.tup.textilapp.controller;

import com.tup.textilapp.model.dto.ResponseMessageDTO;
import com.tup.textilapp.service.ImageDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("image")
public class ImageDataController {
    private final ImageDataService imageDataService;

    public ImageDataController(ImageDataService imageDataService) {
        this.imageDataService = imageDataService;
    }
    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadImage = imageDataService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMessageDTO(uploadImage));
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        byte[] imageData=imageDataService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }
}
