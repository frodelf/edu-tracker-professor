package ua.kpi.edutrackerprofessor.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.kpi.edutrackerprofessor.dto.professor.ProfessorForRegistrationDto;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProfessorValidator implements Validator {
    private final MessageSource messageSource;

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProfessorForRegistrationDto professor = (ProfessorForRegistrationDto) target;
        if(!professor.getPassword().equals(professor.getPasswordRepeat()) && !professor.getPassword().isBlank()){
            errors.rejectValue("password", "", getMessage("error.field.password.repeat"));
        }
    }
}
