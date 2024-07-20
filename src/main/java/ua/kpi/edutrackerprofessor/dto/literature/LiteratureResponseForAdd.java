package ua.kpi.edutrackerprofessor.dto.literature;

import lombok.Data;

@Data
public class LiteratureResponseForAdd {
    private Long id;
    private String name;
    private String link;
    private Long courseId;
    private String courseName;
}