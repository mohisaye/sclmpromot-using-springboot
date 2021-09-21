package com.baeldung.application.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IssueInfo")
public class Issue {

    public Issue() {
    }

    public Issue(String issueKey, String formNumber) {
        this.issueKey = issueKey;
        this.formNumber = formNumber;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "issueKey",nullable = false)
    private String issueKey;
    @Column(name = "formNumber",nullable = false)
    private String formNumber;
    @ManyToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "issue_load", joinColumns = @JoinColumn(name = "issue_id"),
            inverseJoinColumns = @JoinColumn(name = "load_id"))
    List<Load> loads=new ArrayList<>();
    @OneToOne(mappedBy="issue")  //defines a bidirectional relationship.
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getFormNumber() {
        return formNumber;
    }

    public void setFormNumber(String formNumber) {
        this.formNumber = formNumber;
    }

    public List<Load> getLoads() {
        return loads;
    }

    public void setLoads(List<Load> loads) {
        this.loads = loads;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
