package com.tihonovcore.testenv.repository;

import com.tihonovcore.testenv.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("select r from roles r where r.roleName = 'USER'")
    public Role userRole();
}
