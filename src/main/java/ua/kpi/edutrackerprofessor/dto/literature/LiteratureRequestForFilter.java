package ua.kpi.edutrackerprofessor.dto.literature;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class LiteratureRequestForFilter {
    @Min(value = 0, message = "{error.field.min-value}")
    private int page;
    @Min(value = 1, message = "{error.field.min-value}")
    private int pageSize;
    private String name;
    private Long course;
}