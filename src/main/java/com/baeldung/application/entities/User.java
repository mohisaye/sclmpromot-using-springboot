package com.baeldung.application.entities;

import javax.persistence.*;

@Entity
@Table(name = "userInfo")
public class User {

    public User() {
    }

    public User(String name, String issueKey) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "name",nullable = false)
    private String name;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
