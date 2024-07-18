package ua.kpi.edutrackerprofessor.dto.course;

import lombok.Data;

@Data
public class CourseResponseViewAll {
    private Long id;
    private String name;
    private int numberOfStudents;
    private String goal;
    private String image;
}