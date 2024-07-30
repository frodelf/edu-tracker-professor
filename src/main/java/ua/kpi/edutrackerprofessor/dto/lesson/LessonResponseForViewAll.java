package ua.kpi.edutrackerprofessor.dto.lesson;

import lombok.Data;
import ua.kpi.edutrackerentity.entity.enums.StatusLesson;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class LessonResponseForViewAll {
    private Long Id;
    private LocalDateTime date;
    private Long presentStudent;
    private StatusLesson status;
    private Map<String, String> course;
}