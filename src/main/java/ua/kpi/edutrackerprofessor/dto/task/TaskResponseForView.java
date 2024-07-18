package ua.kpi.edutrackerprofessor.dto.task;

import lombok.Data;

import java.util.Map;

@Data
public class TaskResponseForView {
    private Long id;
    private String name;
    private String file;
    private Map<String, String> course;
}