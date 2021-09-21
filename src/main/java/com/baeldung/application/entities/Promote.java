package com.baeldung.application.entities;

import javax.persistence.*;

@Entity
@Table(name = "promoteInfo")
public class Promote {
    public Promote() {
    }

    public Promote(String jobNum, String status, String date, Load load) {
        this.jobNum = jobNum;
        this.status = status;
        Date = date;
        this.load = load;
    }

    @Id
        @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
        private long id;
        @Column(name = "jobNum",nullable = false)
        private String jobNum;
        @Column(name = "status")
        private String status;
        @Column(name = "Date")
        private String Date;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "load_id",referencedColumnName = "id")
    private Load load;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobNum() {
            return jobNum;
        }

        public void setJobNum(String jobNum) {
            this.jobNum = jobNum;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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


