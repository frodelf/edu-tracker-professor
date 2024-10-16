package ua.kpi.edutrackerprofessor.dto.student;

import lombok.Data;

@Data
public class StudentResponseForStatistic {
    private Long id;
    private String groupName;
    private String fullName;
    private String telegram;
    private String numberOfTasksNotDone;
    private String numberOfTasks;
    private String numberOfTasksDone;
    private String mark;
}