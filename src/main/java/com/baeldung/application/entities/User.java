package com.baeldung.application.entities;

import javax.persistence.*;

@Entity
@Table(name = "userInfo")
public class User {

    public User() {
    }

    public User(String userName, String password, Issue issue) {
        this.userName = userName;
        this.password = password;
        this.issue = issue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "userName",nullable = false)
    private String userName;
    @Column(name = "password",nullable = false)
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "issue_id", referencedColumnName = "id")
    private Issue issue;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
