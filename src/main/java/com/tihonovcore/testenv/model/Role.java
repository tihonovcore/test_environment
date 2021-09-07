package com.tihonovcore.testenv.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    private int id;

    @Column(name = "role_name")
    private String roleName;

    public Role() {}

    public Role(String roleName) {
        this.roleName = roleName;
        if (roleName.equals("USER")) {
            id = 1;
        }
    }

    @Override
    public String getAuthority() {
        return roleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
