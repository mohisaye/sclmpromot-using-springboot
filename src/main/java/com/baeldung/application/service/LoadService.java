package com.baeldung.application.service;

import com.baeldung.application.entities.Load;
import com.baeldung.application.entities.Promote;
import com.baeldung.application.repositories.LoadRepository;
import com.baeldung.application.sclm.InputDto;
import com.baeldung.application.sclm.XlsLoadData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class LoadService {
    @Autowired
    private LoadRepository loadRepository;
    @Autowired
    private XlsLoadData readXLS;
    @Autowired
    private Load load;
    @Autowired
    private Promote promote;

    public Optional<Load> findById(Long id) {
        return loadRepository.findById(id);
    }

    public void saveLoad(File excelFile, String source, String bankname,String issueKey) {
        try {
            List<InputDto> loadInfo = readXLS.readXLSXFile2(excelFile);
            for (int i = 0; i < loadInfo.size(); i++) {
                load.setLoadName(loadInfo.get(i).getLoadName());
                load.setLoadSize(loadInfo.get(i).getLoadSize());
                load.setLoadDate(loadInfo.get(i).getLoadDate());
                load.setCicsName(loadInfo.get(i).getCicsName());
                load.setLoadSourcePath(createSourcePath(source, bankname));
                load.setLoadDestinPath(createDestinPath(source,bankname));
                load.setIssueKey(issueKey);
                load.setLoadType("archbind");
                loadRepository.save(load);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createSourcePath(String source, String bankname) {
        String result;
        switch (bankname) {
            case "bmi":
                result = "lbmilib." + source + ".load";
                break;
            case "bsi":
                result = "lbsilib." + source + ".load";
                break;
            case "bts":
                result = "lbtslib." + source + ".load";
                break;
            case "bni":
                result = "lbnilib." + source + ".load";
                break;
            case "tat":
                result = "ltatlib." + source + ".load";
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + bankname);
        }
        return result;
    }

    public String createDestinPath(String source, String bankname) {
        String result;
        String des=null;
        if (source.equals("transit"))
            des = "azmoon";
        else if (source.equals("azmoon")) {
            des = "release";
        }
        switch (bankname) {
            case "bmi":
                result = "lbmilib." + des + ".load";
                break;
            case "bsi":
                result = "lbsilib." + des + ".load";
                break;
            case "bts":
                result = "lbtslib." + des + ".load";
                break;
            case "bni":
                result = "lbnilib." + des + ".load";
                break;
            case "tat":
                result = "ltatlib." + des + ".load";
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + bankname);
        }
        return result;
    }

}
