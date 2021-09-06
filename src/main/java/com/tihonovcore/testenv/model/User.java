package com.tihonovcore.testenv.model;

import javax.persistence.*;
import java.util.List;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Test> author;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_pass_test_with_result",
            joinColumns = { @JoinColumn(name = "uid") },
            inverseJoinColumns = { @JoinColumn(name = "rid" )}
    )
    private List<Result> pass;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Test> getAuthor() {
        return author;
    }

    public void setAuthor(List<Test> author) {
        this.author = author;
    }

    public List<Result> getPass() {
        return pass;
    }

    public void setPass(List<Result> pass) {
        this.pass = pass;
    }
}
