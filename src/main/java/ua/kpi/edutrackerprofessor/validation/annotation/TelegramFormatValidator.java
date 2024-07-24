package ua.kpi.edutrackerprofessor.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class TelegramFormatValidator implements ConstraintValidator<TelegramFormat, String> {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^@[a-zA-Z0-9_]{5,32}$");

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }
}