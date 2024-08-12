package ua.kpi.edutrackerprofessor.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class TelegramFormatValidator implements ConstraintValidator<TelegramFormat, String> {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^@[a-zA-Z0-9_]{5,32}$");

    @Override
    public boolean isValid(String telegram, ConstraintValidatorContext context) {
        if (telegram == null || telegram.isBlank()) {
            return true;
        }
        return USERNAME_PATTERN.matcher(telegram).matches();
    }
}