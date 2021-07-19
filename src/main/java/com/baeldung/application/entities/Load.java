package com.baeldung.application.entities;

import javax.persistence.*;

@Entity
@Table(name = "LoadInfo")
public class Load {


    public Load() {
    }

    public Load(String loadName, String loadDate, String loadSize, String cicsName, String issueKey, String loadSourcePath, String loadDestinPath) {
        this.loadName = loadName;
        this.loadDate = loadDate;
        this.loadSize = loadSize;
        this.cicsName = cicsName;
        this.issueKey = issueKey;
        this.loadSourcePath = loadSourcePath;
        this.loadDestinPath = loadDestinPath;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "loadName",nullable = false)
    private String loadName;
    @Column(name = "loadDate",nullable = false)
    private String loadDate;
    @Column(name = "loadSize",nullable = false)
    private String loadSize;
    @Column(name = "cicsName",nullable = false)
    private String cicsName;
    @Column(name = "issueKey",nullable = false)
    private String issueKey;
    @Column(name = "fromPath",nullable = false)
    private String loadSourcePath;
    @Column(name = "toPath",nullable = false)
    private String loadDestinPath;
    @Column(name = "loadType",nullable = false)
    private String loadType;
    @Column(name = "releaseStat")
    private String releaseStat;
    @OneToOne(mappedBy = "load")
    private Promote promote;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoadName() {
        return loadName;
    }

    public void setLoadName(String loadName) {
        this.loadName = loadName;
    }

    public String getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(String loadDate) {
        this.loadDate = loadDate;
    }

    public String getLoadSize() {
        return loadSize;
    }

    public void setLoadSize(String loadSize) {
        this.loadSize = loadSize;
    }

    public String getCicsName() {
        return cicsName;
    }

    public void setCicsName(String cicsName) {
        this.cicsName = cicsName;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getLoadSourcePath() {
        return loadSourcePath;
    }

    public void setLoadSourcePath(String loadSourcePath) {
        this.loadSourcePath = loadSourcePath;
    }

    public String getLoadDestinPath() {
        return loadDestinPath;
    }

    public void setLoadDestinPath(String loadDestinPath) {
        this.loadDestinPath = loadDestinPath;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    public String getReleaseStat() {
        return releaseStat;
    }

    public void setReleaseStat(String releaseStat) {
        this.releaseStat = releaseStat;
    }

    public Promote getPromote() {
        return promote;
    }
}
