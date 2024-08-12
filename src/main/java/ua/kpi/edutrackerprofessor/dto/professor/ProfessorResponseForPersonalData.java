package ua.kpi.edutrackerprofessor.dto.professor;

import lombok.Data;

@Data
public class ProfessorResponseForPersonalData {
    private Long id;
    private String middleName;
    private String name;
    private String lastName;
    private String degree;
    private String image;
    private String phone;
    private String email;
    private String telegram;
}