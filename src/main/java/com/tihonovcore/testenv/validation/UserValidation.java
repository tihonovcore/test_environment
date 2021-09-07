package com.tihonovcore.testenv.validation;

import com.tihonovcore.testenv.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.stream.Collectors;

public class UserValidation {
    public static String message;

    public static boolean validate(User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        message = validator.validate(user).stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(System.lineSeparator()));

        return !message.isEmpty();

    }
}
