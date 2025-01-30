package ua.kpi.edutrackerprofessor.controller;

import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ua.kpi.edutrackerprofessor.service.MinioService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MinioControllerTest {

    @Mock
    private MinioService minioService;

    @InjectMocks
    private MinioController minioController;

    @Test
    void getImageUrl() throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String imageName = "testImage.jpg";
        String url = "http://minio.server/testImage.jpg";
        when(minioService.getUrl(imageName)).thenReturn(url);

        ResponseEntity<String> result = minioController.getImageUrl(imageName);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(url);
    }

    @Test
    void download() throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String fileName = "testFile.txt";
        byte[] fileData = "file data".getBytes();
        when(minioService.getPhoto(fileName)).thenReturn(fileData);

        ResponseEntity<byte[]> result = minioController.download(fileName);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(fileData);
    }
}
