package com.baeldung.application.dto;

import org.springframework.stereotype.Component;

@Component
public class ResultDto {
    private String loadName;
    private String promoteStatus;
    private String promoteJobNum;
    private String promoteDate;
    private String releaseDate;
    private String releaseToAuth;
    private String releaseStatus;

    public ResultDto() {
        this.loadName = "-";
        this.promoteStatus = "-";
        this.promoteJobNum = "-";
        this.promoteDate = "-";
        this.releaseDate = "-";
        this.releaseToAuth = "-";
        this.releaseStatus = "-";
    }

    public String getLoadName() {
        return loadName;
    }

    public void setLoadName(String loadName) {
        this.loadName = loadName;
    }

    public String getPromoteStatus() {
        return promoteStatus;
    }

    public void setPromoteStatus(String promoteStatus) {
        this.promoteStatus = promoteStatus;
    }

    public String getPromoteJobNum() {
        if (this.promoteJobNum.equals(""))
            setPromoteJobNum("اجرا نشده");
        return promoteJobNum;
    }

    public void setPromoteJobNum(String promoteJobNum) {
        this.promoteJobNum = promoteJobNum;
    }

    public String getPromoteDate() {
        return promoteDate;
    }

    public void setPromoteDate(String promoteDate) {
        this.promoteDate = promoteDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseToAuth() {
        return releaseToAuth;
    }

    public void setReleaseToAuth(String releaseToAuth) {
        this.releaseToAuth = releaseToAuth;
    }

    public String getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(String releaseStatus) {
        this.releaseStatus = releaseStatus;
    }
}
