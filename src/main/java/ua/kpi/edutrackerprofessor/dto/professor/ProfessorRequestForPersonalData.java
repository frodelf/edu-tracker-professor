package ua.kpi.edutrackerprofessor.dto.professor;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.edutrackerprofessor.validation.annotation.EmailFormat;
import ua.kpi.edutrackerprofessor.validation.annotation.ImageExtension;
import ua.kpi.edutrackerprofessor.validation.annotation.PhoneFormat;
import ua.kpi.edutrackerprofessor.validation.annotation.TelegramFormat;

@Data
public class ProfessorRequestForPersonalData {
    private Long id;
    @Size(max = 100, message = "{error.field.size.max}")
    private String middleName;
    @Size(max = 100, message = "{error.field.size.max}")
    private String name;
    @Size(max = 100, message = "{error.field.size.max}")
    private String lastName;
    @Size(max = 100, message = "{error.field.size.max}")
    private String degree;
    @ImageExtension
    private MultipartFile image;
    @PhoneFormat
    private String phone;
    @EmailFormat
    private String email;
    @TelegramFormat
    private String telegram;
}