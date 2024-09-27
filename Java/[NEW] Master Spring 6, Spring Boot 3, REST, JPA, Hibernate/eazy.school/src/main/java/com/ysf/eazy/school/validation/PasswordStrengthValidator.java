package com.ysf.eazy.school.validation;

import com.ysf.eazy.school.annotation.StrongPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class PasswordStrengthValidator implements ConstraintValidator<StrongPassword, String> {

    private List<String> weakPasswords;

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        this.weakPasswords = List.of("12345", "qwerty");
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && !this.weakPasswords.contains(password);
    }
}
