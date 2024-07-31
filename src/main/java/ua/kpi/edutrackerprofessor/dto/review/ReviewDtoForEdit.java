package ua.kpi.edutrackerprofessor.dto.review;

import lombok.Data;
@Data
public class ReviewDtoForEdit {
    private Long id;
    private Long studentId;
    private String fullName;
    private Boolean present;
}