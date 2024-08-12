package ua.kpi.edutrackerprofessor.dto.professor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.kpi.edutrackerprofessor.dto.ContactDataDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProfessorDtoForRegistration extends ContactDataDto {
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 100, message = "{error.field.size.max}")
    private String lastName;
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 100, message = "{error.field.size.max}")
    private String name;
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 100, message = "{error.field.size.max}")
    private String middleName;
    @NotBlank(message = "{error.field.empty}")
    @Size(max = 100, message = "{error.field.size.max}")
    private String degree;
    @Size(min = 8, max = 20, message = "{error.field.size.between}")
    private String passwordForRegistration;
    private String passwordRepeat;
}