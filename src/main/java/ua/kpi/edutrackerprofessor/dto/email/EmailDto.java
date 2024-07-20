package ua.kpi.edutrackerprofessor.dto.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailDto {
    private String group;
    private Long course;
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 100, message = "{error.field.size.max}")
    private String theme;
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 1000, message = "{error.field.size.max}")
    private String message;
}