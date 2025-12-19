package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Валидатор для проверки сложности пароля.
 * Требования:
 * - Минимум 8 символов
 * - Хотя бы одна заглавная буква
 * - Хотя бы одна строчная буква
 * - Хотя бы одна цифра
 * - Хотя бы один специальный символ (!@#$%^&*()_+-=[]{}|;:,.<>?)
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) {
            return false;
        }

        // Минимум 8 символов
        if (password.length() < 8) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пароль должен содержать минимум 8 символов")
                    .addConstraintViolation();
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (specialChars.indexOf(c) >= 0) {
                hasSpecialChar = true;
            }
        }

        if (!hasUpperCase) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пароль должен содержать хотя бы одну заглавную букву")
                    .addConstraintViolation();
            return false;
        }

        if (!hasLowerCase) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пароль должен содержать хотя бы одну строчную букву")
                    .addConstraintViolation();
            return false;
        }

        if (!hasDigit) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пароль должен содержать хотя бы одну цифру")
                    .addConstraintViolation();
            return false;
        }

        if (!hasSpecialChar) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пароль должен содержать хотя бы один специальный символ (!@#$%^&*()_+-=[]{}|;:,.<>?)")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}

