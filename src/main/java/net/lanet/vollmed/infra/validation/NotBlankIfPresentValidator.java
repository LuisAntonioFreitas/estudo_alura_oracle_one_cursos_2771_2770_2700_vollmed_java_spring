package net.lanet.vollmed.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankIfPresentValidator implements ConstraintValidator<NotBlankIfPresent, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Considera-se válido se não está presente
        }
        return !value.trim().isEmpty(); // Verifica se não está em branco se presente
    }
}

