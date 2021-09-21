package com.baeldung.application.service;

import com.baeldung.application.entities.Issue;
import com.baeldung.application.entities.Load;
import com.baeldung.application.entities.Release;
import com.baeldung.application.entities.User;
import com.baeldung.application.repositories.IssueRepository;
import com.baeldung.application.repositories.LoadRepository;
import com.baeldung.application.repositories.ReleaseRepository;
import com.baeldung.application.repositories.UserRepository;
import com.baeldung.application.sclm.BaseMain;
import com.baeldung.application.sclm.InputDto;
import com.baeldung.application.sclm.XlsLoadData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReleaseService {
    @Autowired
    private LoadRepository loadRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReleaseRepository releaseRepository;
    @Autowired
    private XlsLoadData readXLS;
    @Autowired
    private BaseMain main;
    @Autowired
    private Release release;

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

    public int release( String toAuth, String formNumber) {
        List<Issue> issues = issueRepository.findByFormNumber(formNumber);
        User user = userRepository.findByIssue(issues.get(0));
        List<Load> loads=loadRepository.findAllByIssuesAndReleaseTurn(issues.get(0),"1");
        int rc = 0;
        String loadType = "SOURCE";
        String fromAuthCode = "AZM";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        for (int i = 0; i < loads.size(); i++) {
            Release release=new Release();
            String group = loads.get(i).getLoadDestinPath().split("\\.")[1].trim();
            String project = loads.get(i).getLoadDestinPath().split("\\.")[0].trim();
            String gres = main.changeAuthorizationCode(user.getUserName(), user.getPassword(), loads.get(i).getLoadName(), loadType, group, project, fromAuthCode, toAuth);
            rc = Integer.parseInt(gres.split("-num:")[0]);
            release.setLoad(loads.get(i));
            release.setToAuth(toAuth);
            release.setDate(formatter.format(date));
            if (rc != 0) {
                release.setStatus("آزاد سازی با خطا روبرو شد");
            } else {
                release.setStatus("آزاد سازی بدرستی انجام شد");
            }
            releaseRepository.save(release);
        }
      return rc;
    }
}
