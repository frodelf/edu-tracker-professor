package ua.kpi.edutrackerprofessor.dto.literature;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class LiteratureRequestForAdd {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 100, message = "{error.field.size.max}")
    private String name;
    @URL(message = "{error.field.url}")
    @NotBlank(message = "{error.field.empty}")
    private String link;
    @NotNull(message = "{error.field.empty}")
    private Long course;
}