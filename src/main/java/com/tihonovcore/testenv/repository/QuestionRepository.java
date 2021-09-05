package com.tihonovcore.testenv.repository;

import com.tihonovcore.testenv.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
