package com.baeldung.application.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LoadInfo")
public class Load {


    public Load() {
    }

    public Load(String loadName, String loadDate, String loadSize, String cicsName, String loadSourcePath, String loadDestinPath, String loadType) {
        this.loadName = loadName;
        this.loadDate = loadDate;
        this.loadSize = loadSize;
        this.cicsName = cicsName;
        this.loadSourcePath = loadSourcePath;
        this.loadDestinPath = loadDestinPath;
        this.loadType = loadType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private long id;
    @Column(name = "loadName",nullable = false)
    private String loadName;
    @Column(name = "loadDate",nullable = false)
    private String loadDate;
    @Column(name = "loadSize",nullable = false)
    private String loadSize;
    @Column(name = "cicsName",nullable = false)
    private String cicsName;
    @Column(name = "fromPath",nullable = false)
    private String loadSourcePath;
    @Column(name = "toPath",nullable = false)
    private String loadDestinPath;
    @Column(name = "loadType",nullable = false)
    private String loadType;
    @Column(name = "releaseTurn",nullable = false)
    private String releaseTurn;
    @ManyToMany(fetch=FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "loads")
    List<Issue> issues=new ArrayList<>();


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

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public void setReleaseTurn(String releaseTurn) {
        this.releaseTurn = releaseTurn;
    }

    public String getReleaseTurn() {
        return releaseTurn;
    }
}
