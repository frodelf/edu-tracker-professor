package ua.kpi.edutrackerprofessor.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class TaskRequestForAdd {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 100, message = "{error.field.size.max}")
    private String name;
    @NotNull(message = "{error.field.empty}")
    private Long courseId;
    private MultipartFile file;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime deadline;
}