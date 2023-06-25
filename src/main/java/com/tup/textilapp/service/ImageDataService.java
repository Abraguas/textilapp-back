package com.tup.textilapp.service;

import com.tup.textilapp.model.entity.ImageData;
import com.tup.textilapp.repository.ImageDataRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageDataService {
    private final ImageDataRepository imageDataRepository;


    public ImageDataService(ImageDataRepository imageDataRepository) {
        this.imageDataRepository = imageDataRepository;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        if (file.getBytes().length > 2097152 ) {
            throw new IllegalStateException("File too large. Max size is 2MB");
        }
        String[] nameArr = file.getOriginalFilename().split("\\.");
        String fileExtension = nameArr[nameArr.length - 1];
        String fileName = String.join(".", Arrays.copyOf(nameArr, nameArr.length - 1 ));
        while (imageDataRepository.existsByName(String.join(".", fileName, fileExtension))) {
            fileName += "+";
        }
        ImageData imageData = imageDataRepository.save(
                new ImageData(
                        null,
                        String.join(".", fileName, fileExtension),
                        file.getContentType(),
                        compressImage(file.getBytes()))
        );
        return String.join(".", fileName, fileExtension);

    }
    public byte[] downloadImage(String fileName) {
        ImageData dbImageData = imageDataRepository.findByName(fileName)
                .orElseThrow(()-> new EntityNotFoundException("File with name '"+fileName+"' not found"));
        return decompressImage(dbImageData.getImageData());
    }
    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }


    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

}
