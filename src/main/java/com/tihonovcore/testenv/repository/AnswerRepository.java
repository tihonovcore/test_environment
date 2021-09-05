package com.tihonovcore.testenv.repository;

import com.tihonovcore.testenv.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
