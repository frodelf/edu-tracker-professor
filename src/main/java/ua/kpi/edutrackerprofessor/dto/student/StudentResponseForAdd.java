package ua.kpi.edutrackerprofessor.dto.student;

import lombok.Data;

@Data
public class StudentResponseForAdd {
    private Long id;
    private String groupName;
    private String fullName;
    private Boolean present;
}