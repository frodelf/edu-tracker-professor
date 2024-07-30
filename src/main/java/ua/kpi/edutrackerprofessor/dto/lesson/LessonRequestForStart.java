package ua.kpi.edutrackerprofessor.dto.lesson;

import lombok.Data;

import java.util.List;

@Data
public class LessonRequestForStart {
    private String link;
    private Long courseId;
    private List<String> groups;
}