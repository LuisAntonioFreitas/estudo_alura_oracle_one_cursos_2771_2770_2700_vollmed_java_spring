package net.lanet.vollmed.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = EnumValueConstraintValidator.class)
public @interface EnumValueConstraint {

    String message() default "O valor informado não é válido.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> enumClass();
}
