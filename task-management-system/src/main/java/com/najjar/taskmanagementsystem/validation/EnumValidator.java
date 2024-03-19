package com.najjar.taskmanagementsystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValue, CharSequence> {

    private EnumValue annotation;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        boolean isValid = false;
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();

        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value.toString().equals(enumValue.toString())) {
                    isValid = true;
                    break;
                }
            }
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(
                            "Invalid value. Must be one of: " + this.getEnumValues(enumValues))
                    .addConstraintViolation();
        }

        return isValid;
    }

    private String getEnumValues(Object[] enumValues) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < enumValues.length; i++) {
            result.append(enumValues[i]);
            if (i < enumValues.length - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }
}
