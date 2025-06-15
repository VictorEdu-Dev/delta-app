package org.deltacore.delta.dto.tutoring;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.deltacore.delta.model.tutoring.Modality;

public class ModalityValidator implements ConstraintValidator<GabeModality, String> {
    @Override
    public void initialize(GabeModality constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        try {
            Modality.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
