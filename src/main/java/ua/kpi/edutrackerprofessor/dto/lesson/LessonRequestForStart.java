package ua.kpi.edutrackerprofessor.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
public class LessonRequestForStart {
    @URL(message = "{error.field.url}")
    @NotBlank(message = "{error.field.empty}")
    private String link;
    @NotNull(message = "{error.field.empty}")
    private Long courseId;
    private List<String> groups;
}