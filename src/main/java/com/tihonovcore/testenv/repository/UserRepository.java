package com.tihonovcore.testenv.repository;

import com.tihonovcore.testenv.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByName(String name);
}
