package ua.kpi.edutrackerprofessor.dto.student;

import lombok.Data;

import java.util.Map;

@Data
public class StudentResponseViewAll {
    private Long id;
    private String group;
    private String middleName;
    private String name;
    private String lastName;
    private String telegram;
    private String phone;
    private Map<String, String> courses;
}