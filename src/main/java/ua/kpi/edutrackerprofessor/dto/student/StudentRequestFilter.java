package ua.kpi.edutrackerprofessor.dto.student;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentRequestFilter {
    @Min(value = 0, message = "{error.field.min-value}")
    private int page;
    @Min(value = 1, message = "{error.field.min-value}")
    private int pageSize;
    @Size(max = 100, message = "{error.field.size.max}")
    private String group;
    @Size(max = 100, message = "{error.field.size.max}")
    private String fullName;
    private Long course;
    @Size(max = 100, message = "{error.field.size.max}")
    private String telegram;
    @Size(max = 100, message = "{error.field.size.max}")
    private String phone;
}