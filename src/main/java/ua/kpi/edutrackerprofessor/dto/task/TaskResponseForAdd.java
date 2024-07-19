package ua.kpi.edutrackerprofessor.dto.task;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponseForAdd {
    private Long id;
    private String name;
    private Long courseId;
    private String courseName;
    private LocalDateTime deadline;
}