package ua.kpi.edutrackerprofessor.dto.professor;

import lombok.Data;

@Data
public class ProfessorResponseForGlobal {
    private Long id;
    private String lastName;
    private String name;
    private String email;
    private String degree;
    private String image;
}