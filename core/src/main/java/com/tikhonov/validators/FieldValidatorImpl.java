package com.tikhonov.validators;

import org.springframework.stereotype.Component;

import javax.validation.Validator;

@Component
public class FieldValidatorImpl implements FieldValidator {

    private final Validator validator;

    public FieldValidatorImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public boolean validate(Object obj) {
        return validator.validate(obj).size() == 0;
    }
}
