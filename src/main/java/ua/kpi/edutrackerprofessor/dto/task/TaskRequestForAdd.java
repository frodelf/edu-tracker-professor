package ua.kpi.edutrackerprofessor.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.edutrackerprofessor.validation.annotation.ImageExtension;


@Data
public class TaskRequestForAdd {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 100, message = "{error.field.size.max}")
    private String name;
    @NotNull(message = "{error.field.empty}")
    private Long courseId;
    @NotNull(message = "{error.file.empty}")
    private MultipartFile file;
}