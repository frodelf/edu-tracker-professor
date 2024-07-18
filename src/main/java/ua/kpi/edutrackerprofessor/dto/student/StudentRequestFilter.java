package ua.kpi.edutrackerprofessor.dto.student;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class StudentRequestFilter {
    @Min(value = 0, message = "{error.field.min-value}")
    private int page;
    @Min(value = 1, message = "{error.field.min-value}")
    private int pageSize;
    private String group;
    private String fullName;
    private Long course;
    private String telegram;
    private String phone;
}