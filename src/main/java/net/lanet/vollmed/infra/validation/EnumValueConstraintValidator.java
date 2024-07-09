package net.lanet.vollmed.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EnumValueConstraintValidator implements ConstraintValidator<EnumValueConstraint, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumValueConstraint constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Allow null values
        }

        Pattern pattern = Pattern.compile(generateRegexFromEnum(enumClass));
        return pattern.matcher(value).matches();
    }

    private String generateRegexFromEnum(Class<? extends Enum<?>> enumClass) {
        StringBuilder regex = new StringBuilder("^(");

        for (Enum<?> constant : enumClass.getEnumConstants()) {
            regex.append("|" + Pattern.quote(constant.name()));
        }

        regex.delete(0, 1).append(")$");

        return regex.toString();
    }
}
