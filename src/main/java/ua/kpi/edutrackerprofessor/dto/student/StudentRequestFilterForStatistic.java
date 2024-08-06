package ua.kpi.edutrackerprofessor.dto.student;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentRequestFilterForStatistic {
    @Min(value = 0, message = "{error.field.min-value}")
    private int page;
    @Min(value = 0, message = "{error.field.min-value}")
    private int pageSize;
    @Size(max = 100, message = "{error.field.size.max}")
    private String search;
    @NotNull(message = "{Please fill in the field!}")
    private Long courseId;
}