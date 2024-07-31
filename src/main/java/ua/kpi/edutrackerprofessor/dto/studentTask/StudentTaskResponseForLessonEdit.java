package ua.kpi.edutrackerprofessor.dto.studentTask;

import lombok.Data;
import ua.kpi.edutrackerentity.entity.enums.StatusStudentsTask;

@Data
public class StudentTaskResponseForLessonEdit {
    private Long id;
    private Long taskId;
    private Long studentId;
    private StatusStudentsTask status;
    private Double mark;
    private String myWork;
    private String taskName;
}