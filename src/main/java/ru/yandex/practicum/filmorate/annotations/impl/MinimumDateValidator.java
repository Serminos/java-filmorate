package ru.yandex.practicum.filmorate.annotations.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotations.MinDate;

import java.time.LocalDate;

public class MinimumDateValidator implements ConstraintValidator<MinDate, LocalDate> {
    private LocalDate minimumDate;

    @Override
    public void initialize(MinDate constraintAnnotation) {
        minimumDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || !value.isBefore(minimumDate);
    }
}
