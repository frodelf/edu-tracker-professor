package ua.kpi.edutrackerprofessor.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneFormatValidator implements ConstraintValidator<PhoneFormat, String> {
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+380(31|32|33|34|35|36|37|38|39|41|42|43|44|45|46|47|48|49|50|59|61|63|66|67|68|73|89|91|92|93|94|95|96|97|98|99)\\d{7}$"
    );
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isBlank()) {
            return true;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
}
