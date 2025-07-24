package org.deltacore.delta.domains.activity.dto;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.Builder;
import org.deltacore.delta.domains.activity.model.ActivityStatus;
import org.deltacore.delta.domains.activity.model.ActivityType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Builder
public record ActivityFilterDTO(
        @ValidActivityStatus
        String status,
        @ValidActivityType
        String activityType
) {
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Constraint(validatedBy = ActivityTypeValidator.class)
    @Documented
    public @interface ValidActivityType {
        String message() default "Invalid ActivityType: '${validatedValue}'";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Constraint(validatedBy = ActivityStatusValidator.class)
    @Documented
    public @interface ValidActivityStatus {
        String message() default "Invalid ActivityStatus: '${validatedValue}'";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
    private static class ActivityStatusValidator implements ConstraintValidator<ValidActivityStatus, String> {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.trim().isEmpty()) return true;
            return Arrays.stream(ActivityStatus.values())
                    .anyMatch(e -> e.name().equalsIgnoreCase(value));
        }
    }

    private static class ActivityTypeValidator implements ConstraintValidator<ValidActivityType, String> {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.trim().isEmpty()) return true;
            return Arrays.stream(ActivityType.values())
                    .anyMatch(e -> e.name().equalsIgnoreCase(value));
        }
    }

}
