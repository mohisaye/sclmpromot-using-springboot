package com.baeldung.application.service;

import com.baeldung.application.sclm.BaseMain;
import com.baeldung.application.sclm.InputDto;
import com.baeldung.application.sclm.XlsLoadData;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

import static com.baeldung.application.sclm.XlsLoadData.writeXLSXFile;

@Service
public class ReleaseService {

    public int loadRelease(File excelFile, String user, String pass, String toAuthCode) {
        int res = 0;
        XlsLoadData loadData=new XlsLoadData();
        try {
            List<InputDto> loadInfo = loadData.readXLSXFile2(excelFile);
            BaseMain main = new BaseMain();
            for (int i = 0; i < loadInfo.size(); i++) {
                System.out.println(loadInfo.get(i).getRowId() + "*****************");
                String project = loadInfo.get(i).getLoadPath().split("\\.")[0].trim();
                String group = loadInfo.get(i).getLoadPath().split("\\.")[1].trim();
                String loadName = loadInfo.get(i).getLoadName();
                String loadType = "SOURCE";
                String fromAuthCode = "AZM";
                if (main.isLoadExist(user, pass, loadInfo.get(i).getLoadPath(), loadInfo.get(i).getLoadName())) {
                    String gres = main.changeAuthorizationCode(user, pass, loadName, loadType, group, project, fromAuthCode, toAuthCode);
                    int rc=Integer.parseInt(gres.split("-num:")[0]);
                    if (rc != 0) {
                        writeXLSXFile(excelFile, loadName, 7,"");
                    } else {
                        writeXLSXFile(excelFile, loadName, 8,"");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
