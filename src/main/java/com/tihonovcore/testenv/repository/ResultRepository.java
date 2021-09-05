package com.tihonovcore.testenv.repository;

import com.tihonovcore.testenv.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result, Integer> {
}
