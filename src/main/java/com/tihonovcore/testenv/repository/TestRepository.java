package com.tihonovcore.testenv.repository;

import com.tihonovcore.testenv.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Integer> {
}
