package com.baeldung.application.entities;

import javax.persistence.*;

@Entity
@Table(name = "ReleaseInfo")
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "status")
    private String status;
    @Column(name = "toAuth")
    private String toAuth;
    @Column(name = "Date")
    private String Date;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "load_id", referencedColumnName = "id")
    private Load load;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToAuth() {
        return toAuth;
    }

    public void setToAuth(String toAuth) {
        this.toAuth = toAuth;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Load getLoad() {
        return load;
    }

    public void setLoad(Load load) {
        this.load = load;
    }
}
