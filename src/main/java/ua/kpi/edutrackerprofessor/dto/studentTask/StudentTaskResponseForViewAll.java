package ua.kpi.edutrackerprofessor.dto.studentTask;

import lombok.Data;
import ua.kpi.edutrackerentity.entity.enums.StatusStudentsTask;

@Data
public class StudentTaskResponseForViewAll {
    private Long id;
    private String groupName;
    private String fullName;
    private String telegram;
    private StatusStudentsTask status;
    private Double mark;
    private String myWork;
}