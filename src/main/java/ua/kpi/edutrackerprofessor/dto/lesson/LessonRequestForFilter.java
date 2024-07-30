package ua.kpi.edutrackerprofessor.dto.lesson;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ua.kpi.edutrackerentity.entity.enums.StatusLesson;

import java.time.LocalDateTime;

@Data
public class LessonRequestForFilter {
    @Min(value = 0, message = "{error.field.min-value}")
    private int page;
    @Min(value = 1, message = "{error.field.min-value}")
    private int pageSize;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime date;
    private Long courseId;
    private Long presentStudents;
    private StatusLesson statusLesson;
}