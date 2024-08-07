package ua.kpi.edutrackerprofessor.dto.task;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ua.kpi.edutrackerentity.entity.enums.StatusTask;

import java.time.LocalDateTime;

@Data
public class TaskRequestFilter {
    @Min(value = 0, message = "{error.field.min-value}")
    private Integer page;
    @Min(value = 1, message = "{error.field.min-value}")
    private Integer pageSize;
    @Size(max = 100, message = "{error.field.size.max}")
    private String name;
    private Long courseId;
    private StatusTask status;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime deadline;
}