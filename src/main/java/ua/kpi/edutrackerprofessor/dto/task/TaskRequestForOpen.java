package ua.kpi.edutrackerprofessor.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class TaskRequestForOpen {
    @NotNull(message = "{error.field.empty}")
    private Long taskId;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime deadline;
}