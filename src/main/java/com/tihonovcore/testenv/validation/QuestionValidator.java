package com.tihonovcore.testenv.validation;

import com.tihonovcore.testenv.model.Answer;
import com.tihonovcore.testenv.model.Question;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionValidator {
    public static List<String> messages;

    public static boolean validate(Question question) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        long countCorrectAnswers = question.getAnswers().stream().filter(Answer::isCorrect).count();
        messages = question.getAnswers().stream().map(a -> {
            List<String> message = validator.validate(a).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());

            if (countCorrectAnswers > 1 && a.isCorrect() || countCorrectAnswers == 0) {
                message.add("Expected exact one correct answer");
            }

            return String.join(", ", message);
        }).collect(Collectors.toList());

        //default values
        messages.addAll(List.of("", "", "", ""));

        return messages.stream().anyMatch(m -> !m.isEmpty());
    }
}
