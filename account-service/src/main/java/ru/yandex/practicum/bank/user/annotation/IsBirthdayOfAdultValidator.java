package ru.yandex.practicum.bank.user.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.bank.user.exception.UserNotIsOfLegalAgeException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class IsBirthdayOfAdultValidator implements ConstraintValidator<IsBirthdayOfAdult, LocalDate> {

    private static final int AGE_OF_MAJORITY = 18;

    @Override
    public void initialize(IsBirthdayOfAdult contactNumber) {
    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext constraintValidatorContext) {
        if (birthdate != null && ChronoUnit.YEARS.between(birthdate, LocalDate.now()) >= AGE_OF_MAJORITY) {
            return true;
        }
        throw new UserNotIsOfLegalAgeException();
    }

}