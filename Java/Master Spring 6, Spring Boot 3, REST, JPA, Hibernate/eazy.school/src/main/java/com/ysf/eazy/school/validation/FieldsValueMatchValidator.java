package com.ysf.eazy.school.validation;

import com.ysf.eazy.school.annotation.FieldsValueMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {

    private String field;
    private String matchField;

    @Override
    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.matchField = constraintAnnotation.matchField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl objToValidate = new BeanWrapperImpl(value);
        Object fieldValue = objToValidate.getPropertyValue(field);
        Object matchFieldValue = objToValidate.getPropertyValue(matchField);

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode(this.field)
                .addConstraintViolation();

        return fieldValue == null || fieldValue.equals(matchFieldValue);
    }
}
