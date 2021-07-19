package com.baeldung.application.service;

import com.baeldung.application.entities.Load;
import com.baeldung.application.entities.Promote;
import com.baeldung.application.repositories.LoadRepository;
import com.baeldung.application.repositories.PromoteRepository;
import com.baeldung.application.sclm.BaseMain;
import com.baeldung.application.sclm.JiraUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class PromoteService {

    @Autowired
    private BaseMain baseMain;
    @Autowired
    private PromoteRepository promoteRepository;
    @Autowired
    private LoadRepository loadRepository;
    @Autowired
    private Promote promote;
    @Autowired
    private JiraUtils jiraUtils;

    public boolean checkExist(String user, String pass, String loadpath, String loadname) {

        boolean _isExist = false;
        try {
            _isExist = baseMain.isLoadExist(user, pass, loadpath, loadname);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return _isExist;
    }

    public boolean checkSize(String user, String pass, String loadpath, String loadname, String loadsize) {

        boolean _isSizeEq = false;
        try {
            boolean _isExist = baseMain.isLoadExist(user, pass, loadpath, loadname);
            if (_isExist) {
                _isSizeEq = baseMain.getSizeFtp(user, pass, loadpath, loadname, loadsize);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return _isSizeEq;
    }

    public boolean checkDate(String loaddate, String loadname) {
        boolean _isDateEq = false;
        try {
            _isDateEq = baseMain.compareDate(loaddate, loadname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _isDateEq;
    }

    public void savePromote(String issueKey, String jobNum, Load load, String status) {
        promote.setIssueKey(issueKey);
        promote.setJobNum(jobNum);
        promote.setLoad(load);
        promote.setStatus(status);
        try {
            promoteRepository.save(promote);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void promote(String user, String pass, String issueKey) {
        List<Load> loads = loadRepository.findByIssueKey(issueKey);
        Assert.notNull(loads, "loads must not be null!");
        Assert.isTrue(!(loads.size() == 0), "loads must not be empty");
        for (Load l : loads) {
            Promote promote = promoteRepository.findByIssueKeyAndLoad(issueKey, l);
            if (promote.getJobNum().equals("") && promote.getStatus().equals("")) {
                String project = l.getLoadSourcePath().split("\\.")[0].trim();
                String group = l.getLoadSourcePath().split("\\.")[1].trim();
                if (checkExist(user, pass, l.getLoadSourcePath(), l.getLoadName())) {
                    if (checkSize(user, pass, l.getLoadSourcePath(), l.getLoadName(), l.getLoadSize())) {
                        if (checkDate(l.getLoadDate(), l.getLoadName())) {
                            String gres = baseMain.doPromote(user, pass, l.getLoadName(), project, group, l.getLoadType());
                            savePromote(issueKey, gres.split("-num:")[1], l, gres.split("-num:")[0]);
                        } else {
                            savePromote(issueKey, "", l, "Date is not equal");
                        }
                    } else {
                        savePromote(issueKey, "", l, "Size is not equal");
                    }
                } else {
                    savePromote(issueKey, "", l, "load not exist");
                }
            }

        }

    }

    public void promoteResult(String issueKey, String formNum) {
        String jiraIssue = "http://172.17.6.160:8080/browse/" + issueKey;
        String res = "<p>" + jiraIssue + "</p><br/>" + "<tr><td>" + "اسم لود" + "</td><td>" + "وضعیت پروموت" + "</td><td>" + "شماره جاب" + "</td></tr>";
//        int responsecode = -1;
        try {
            List<Promote> result = promoteRepository.findAllByIssueKey(issueKey);

            for (Promote prom : result) {
                if (prom.getJobNum().equals(""))
                    prom.setJobNum("اجرا نشده");
                String line = "<tr><td>" + prom.getLoad().getLoadName() + "</td><td>" + prom.getStatus() + "</td><td>" + prom.getJobNum() + "</td></tr>";
                res += line;
            }
            jiraUtils.addPageAndContent("<p><table>" + res + "</table></p>", formNum, 0);
//            responsecode= response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
