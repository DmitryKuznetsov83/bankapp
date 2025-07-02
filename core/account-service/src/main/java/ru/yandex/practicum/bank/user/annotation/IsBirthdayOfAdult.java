package ru.yandex.practicum.bank.user.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsBirthdayOfAdultValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsBirthdayOfAdult {
    String message() default "User is not adult";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
