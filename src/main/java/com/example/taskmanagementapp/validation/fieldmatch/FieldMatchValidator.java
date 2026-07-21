package com.example.taskmanagementapp.validation.fieldmatch;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String field;
    private String repeatedField;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.repeatedField = constraintAnnotation.repeatedField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        Object repeatedFieldValue = new BeanWrapperImpl(value).getPropertyValue(repeatedField);
        if (fieldValue == null) {
            return repeatedFieldValue == null;
        }

        return fieldValue.equals(repeatedFieldValue);
    }
}
