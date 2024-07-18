package ua.kpi.edutrackerprofessor.service;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface MinioService {
    byte[] getPhoto(String objectName) throws ErrorResponseException, InsufficientDataException
            , InternalException, InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException, ServerException
            , XmlParserException, IOException;
    //TODO доробити видалення файлів при оновленні якогось об'кта
    void deleteImg(String objectName, String directory) throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IOException;
    String putMultipartFile(MultipartFile multipartFile) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    String getUrl(String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}