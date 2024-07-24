package ua.kpi.edutrackerprofessor.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhoneFormatValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneFormat {
    String message() default "{error.field.telephone.format}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}