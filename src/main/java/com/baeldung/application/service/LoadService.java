package com.baeldung.application.service;

import com.baeldung.application.entities.Issue;
import com.baeldung.application.entities.Load;
import com.baeldung.application.repositories.IssueRepository;
import com.baeldung.application.repositories.LoadRepository;
import com.baeldung.application.sclm.InputDto;
import com.baeldung.application.sclm.XlsLoadData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoadService {
    @Autowired
    private  LoadRepository loadRepository;
    @Autowired
    private  IssueRepository issueRepository;
    @Autowired
    private  XlsLoadData readXLS;


    public Optional<Load> findById(Long id) {
        return loadRepository.findById(id);
    }

public void updateLoad(Load load){loadRepository.saveAndFlush(load);}

    public void saveLoad(File excelFile, String source, String bankname, String issueKey) {
        try {
            List<InputDto> loadInfo = readXLS.readXLSXFile2(excelFile);
            List<Load> loads = new ArrayList<>();
            Issue issue=issueRepository.findByIssueKey(issueKey);
            for (InputDto inputDto : loadInfo) {
                Load load = new Load(inputDto.getLoadName(), inputDto.getLoadDate(), inputDto.getLoadSize()
                        , createCicsName(bankname), createSourcePath(source, bankname), createDestinPath(source, bankname), "ARCHBIND");
                load.getIssues().add(issue);
                loads.add(load);
            }
            Thread.sleep(1000);
            loadRepository.saveAll(loads);
            issue.getLoads().addAll(loads);
            issueRepository.save(issue);
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
        String des = null;
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

    public String createCicsName(String bankname) {
        String result;

        switch (bankname) {
            case "bmi":
                result = "CICSBMI";
                break;
            case "bsi":
                result = "CICSBSI";
                break;
            case "bts":
                result = "CICSBTS";
                break;
            case "bni":
                result = "CICSBN";
                break;
            case "tat":
                result = "CICSTAT";
                break;
            case "btj":
                result = "CICSBTJ";
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + bankname);
        }
        return result;
    }

    public List<Load> findLoads(File excelfile, String formNumber) {
        List<Load> loads = new ArrayList<>();
        try {
            List<Issue> issues = issueRepository.findByFormNumber(formNumber);
            List<InputDto> loadInfo = readXLS.readXLSXFile2(excelfile);
            for (int i = 0; i < loadInfo.size(); i++) {
                loads = loadRepository.findByIssuesInAndLoadName(issues, loadInfo.get(i).getLoadName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loads;
    }

}
