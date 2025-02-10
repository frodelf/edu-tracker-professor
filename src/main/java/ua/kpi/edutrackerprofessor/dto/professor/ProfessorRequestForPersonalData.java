package ua.kpi.edutrackerprofessor.dto.professor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.edutrackerprofessor.dto.ContactDataDto;
import ua.kpi.edutrackerprofessor.validation.annotation.ImageExtension;

@Data
public class ProfessorRequestForPersonalData extends ContactDataDto {
    @Size(max = 100, message = "{error.field.size.max}")
    @NotBlank(message = "{error.field.empty}")
    private String middleName;
    @Size(max = 100, message = "{error.field.size.max}")
    @NotBlank(message = "{error.field.empty}")
    private String name;
    @Size(max = 100, message = "{error.field.size.max}")
    @NotBlank(message = "{error.field.empty}")
    private String lastName;
    @Size(max = 100, message = "{error.field.size.max}")
    private String degree;
    @ImageExtension
    private MultipartFile image;
}