package com.baeldung.application.sclm;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by m_sayekooie on 05/06/2019.
 */
public class InputDto implements Serializable {
    public String loadName;
    public String loadSize;
    public String loadDate;
    public String loadPath;
    public String loadType;
    public String cicsName;
    public String rowId;

    @Override
    public String toString() {
        return "InputDto{" +
                "loadName='" + loadName + '\'' +
                ", loadSize='" + loadSize + '\'' +
                ", loadDate='" + loadDate + '\'' +
                ", loadPath='" + loadPath + '\'' +
                ", loadType='" + loadType + '\'' +
                ", rowId='" + rowId + '\'' +
                '}';
    }

    public String getLoadName() {
        return loadName;
    }

    public void setLoadName(String loadName) {
        this.loadName = loadName.trim().toUpperCase();
    }

    public String getLoadSize() {
        return loadSize;
    }

    public void setLoadSize(String loadSize) {
        if (!loadSize.equals("") && !loadSize.equals(null)) {
            loadSize = StringUtils.leftPad(loadSize, 8, '0');
            this.loadSize = loadSize.trim().toUpperCase();
        }
    }

    public String getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(String loadDate) {
        if (!loadDate.equals("") && !loadDate.equals(null))
            this.loadDate = loadDate.trim().toUpperCase();
    }

    public String getLoadPath() {
        return loadPath;
    }

    public void setLoadPath(String loadPath) {
        if (!loadPath.equals("") && !loadPath.equals(null))
            this.loadPath = loadPath.trim().toUpperCase();
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        if (!loadType.equals("") && !loadType.equals(null))
            this.loadType = loadType.trim().toUpperCase();
        else
            this.loadType ="ARCHBIND";
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getCicsName() {
        return cicsName;
    }

    public void setCicsName(String cicsName) {
        if (!cicsName.equals("") && !cicsName.equals(null))
            this.cicsName = cicsName;
    }
}
