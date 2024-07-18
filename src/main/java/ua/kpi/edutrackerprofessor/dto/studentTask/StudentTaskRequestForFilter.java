package ua.kpi.edutrackerprofessor.dto.studentTask;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    private String groupName;
    private String fullName;
    private String telegram;
    private StatusStudentsTask status;
}