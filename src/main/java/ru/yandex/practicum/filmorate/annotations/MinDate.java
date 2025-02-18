package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.Constraint;
import ru.yandex.practicum.filmorate.annotations.impl.MinimumDateValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinimumDateValidator.class)
@Documented
public @interface MinDate {
    String message() default "Date must not be before {value}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    String value() default "1895-12-28";
}
