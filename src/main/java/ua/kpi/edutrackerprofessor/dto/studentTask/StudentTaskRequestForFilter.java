package ua.kpi.edutrackerprofessor.dto.studentTask;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ua.kpi.edutrackerentity.entity.enums.StatusStudentsTask;

@Data
public class StudentTaskRequestForFilter {
    @Min(value = 0, message = "{error.field.min-value}")
    private int page;
    @Min(value = 1, message = "{error.field.min-value}")
    private int pageSize;
    @NotNull(message = "{error.field.empty}")
    private Long taskId;
    @Size(max = 100, message = "{error.field.size.max}")
    private String groupName;
    @Size(max = 100, message = "{error.field.size.max}")
    private String fullName;
    @Size(max = 100, message = "{error.field.size.max}")
    private String telegram;
    private StatusStudentsTask status;
}