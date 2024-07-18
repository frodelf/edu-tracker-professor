package ua.kpi.edutrackerprofessor.dto.student;

import lombok.Data;

@Data
public class StudentResponseViewOnePage {
    private Long id;
    private String lastName;
    private String name;
    private String middleName;
    private String phone;
    private String email;
    private String telegram;
    private String groupName;
}