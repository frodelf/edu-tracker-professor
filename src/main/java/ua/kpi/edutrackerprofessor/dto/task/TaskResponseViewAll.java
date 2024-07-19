package ua.kpi.edutrackerprofessor.dto.task;

import lombok.Data;
import ua.kpi.edutrackerentity.entity.enums.StatusTask;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class TaskResponseViewAll {
    private Long id;
    private String name;
    private Map<String, String> course;
    private StatusTask status;
    private LocalDateTime deadline;
}