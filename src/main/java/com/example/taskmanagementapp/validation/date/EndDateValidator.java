package com.example.taskmanagementapp.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import org.springframework.beans.BeanWrapperImpl;

public class EndDateValidator implements ConstraintValidator<EndDate, Object> {
    private String startDate;
    private String endDate;

    @Override
    public void initialize(EndDate constraintAnnotation) {
        this.startDate = constraintAnnotation.startDateField();
        this.endDate = constraintAnnotation.endDateField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(startDate);
        Object repeatedFieldValue = new BeanWrapperImpl(value).getPropertyValue(endDate);
        if (fieldValue == null || repeatedFieldValue == null) {
            return true;
        }

        LocalDateTime startDateValue = (LocalDateTime) fieldValue;
        LocalDateTime endDateValue = (LocalDateTime) repeatedFieldValue;

        return endDateValue.isAfter(startDateValue);
    }
}
