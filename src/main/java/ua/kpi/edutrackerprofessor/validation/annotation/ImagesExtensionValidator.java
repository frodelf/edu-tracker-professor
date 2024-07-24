package ua.kpi.edutrackerprofessor.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ImagesExtensionValidator implements ConstraintValidator<ImageExtension, MultipartFile> {
    private final List<String> supportedImageFormats = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/gif");
    @Override
    public boolean isValid(MultipartFile multipartFiles, ConstraintValidatorContext constraintValidatorContext) {
        if(multipartFiles == null)return true;
        if (!multipartFiles.isEmpty()) {
            return supportedImageFormats.contains(multipartFiles.getContentType());
        }
        return true;
    }
}
