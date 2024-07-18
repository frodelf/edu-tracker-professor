package ua.kpi.edutrackerprofessor.controller;

import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import ua.kpi.edutrackerprofessor.service.MinioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/minio")
@RequiredArgsConstructor
public class MinioController {
    private final MinioService minioService;
    @GetMapping("/get-image")
    public ResponseEntity<String> getImageUrl(@RequestParam String imageName) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(minioService.getUrl(imageName));
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(minioService.getPhoto(fileName));
    }
}