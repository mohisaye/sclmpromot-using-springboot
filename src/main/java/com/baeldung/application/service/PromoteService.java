package com.baeldung.application.service;

import com.baeldung.application.dto.ResultDto;
import com.baeldung.application.entities.*;
import com.baeldung.application.repositories.*;
import com.baeldung.application.sclm.BaseMain;
import com.baeldung.application.sclm.JiraUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PromoteService {
    @Autowired
    private  BaseMain baseMain;
    @Autowired
    private  PromoteRepository promoteRepository;
    @Autowired
    private  IssueRepository issueRepository;
    @Autowired
    private  UserRepository userRpository;
    @Autowired
    private  ReleaseRepository releaseRepository;
    @Autowired
    private Promote promote;
    @Autowired
    private Release release;
    @Autowired
    private  JiraUtils jiraUtils;


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


    public void savePromote(String jobNum, Load load, String status) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Promote promote=new Promote(jobNum,status,formatter.format(date),load);
//        promote.setJobNum(jobNum);
//        promote.setStatus(status);
//        promote.setDate(formatter.format(date));
//        promote.setLoad(load);
        promoteRepository.save(promote);
    }

    public void updatePromote(Long id, String status) {
         promoteRepository.existsById(id);
    }

    public void promote(String issueKey) {
        Issue issue = issueRepository.findByIssueKey(issueKey);
        User user = userRpository.findByIssue(issue);
        List<Load> loads = user.getIssue().getLoads();//loadRepository.findByIssues(issue);
        Assert.notNull(loads, "loads must not be null!");
        Assert.isTrue(!(loads.size() == 0), "loads must not be empty");
        try {
            for (Load l : loads) {
                Promote promote = promoteRepository.findByLoad(l);
                if (promote == null) {
                    String project = l.getLoadSourcePath().split("\\.")[0].trim();
                    String group = l.getLoadSourcePath().split("\\.")[1].trim();
                    if (checkExist(user.getUserName(), user.getPassword(), l.getLoadSourcePath(), l.getLoadName())) {
                        if (checkSize(user.getUserName(), user.getPassword(), l.getLoadSourcePath(), l.getLoadName(), l.getLoadSize())) {
                            baseMain.readLoadFromIbm(l.getLoadName(), l.getLoadSourcePath(), user.getUserName(), user.getPassword());
                            if (checkDate(l.getLoadDate(), l.getLoadName())) {
                                String gres = baseMain.doPromote(user.getUserName(), user.getPassword(), l.getLoadName(), project, group, l.getLoadType());
                                savePromote(gres.split("-num:")[1], l, gres.split("-num:")[0]);
                            } else {
                                savePromote("", l, "Date is not equal");
                            }
                        } else {
                            savePromote("", l, "Size is not equal");
                        }
                    } else {
                        savePromote("", l, "load not exist");
                    }
                } else if (promote.getJobNum().equals("")) {
                    promote = promoteRepository.findByLoad(l);
                    String project = l.getLoadSourcePath().split("\\.")[0].trim();
                    String group = l.getLoadSourcePath().split("\\.")[1].trim();
                    if (checkExist(user.getUserName(), user.getPassword(), l.getLoadSourcePath(), l.getLoadName())) {
                        if (checkSize(user.getUserName(), user.getPassword(), l.getLoadSourcePath(), l.getLoadName(), l.getLoadSize())) {
                            baseMain.readLoadFromIbm(l.getLoadName(), l.getLoadSourcePath(), user.getUserName(), user.getPassword());
                            if (checkDate(l.getLoadDate(), l.getLoadName())) {
                                String gres = baseMain.doPromote(user.getUserName(), user.getPassword(), l.getLoadName(), project, group, l.getLoadType());
                                savePromote(gres.split("-num:")[1], l, gres.split("-num:")[0]);
                            } else {

                                updatePromote(promote.getId(), "Date is not equal");
                            }
                        } else {
                            updatePromote(promote.getId(), "Size is not equal");
                        }
                    } else {
                        updatePromote(promote.getId(), "load not exist");
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void promoteResult(String issueKey, String formNum) {
//        String jiraIssue = "http://172.17.6.160:8080/browse/" + issueKey;
        StringBuilder res = new StringBuilder("<br/><p><ac:structured-macro ac:name='jira' ac:schema-version='1' ac:macro-id='ce876bed-eeb4-4592-9255-a3a216c1d507'><ac:parameter ac:name='server'>172.17.6.160:8080</ac:parameter><ac:parameter ac:name='serverId'></ac:parameter><ac:parameter ac:name='key'>" + issueKey + "</ac:parameter></ac:structured-macro></p><br/><br/><br/>" + "<table><thead><tr><td>" + "اسم لود" + "</td><td>" + "وضعیت پروموت" + "</td><td>" + "شماره جاب"
                + "</td><td>" + "تاریخ پروموت" + "</td><td>" + "وضعیت آزادسازی" + "</td><td>" + "تاریخ آزاد سازی" + "</td><td>" + "آزادسازی برای" + "</td></tr></thead><tbody>");

        try {
            Issue issue = issueRepository.findByIssueKeyAndFormNumber(issueKey, formNum);
            List<Load> loads = issue.getLoads(); //loadRepository.findByIssues(issues.get(0));
            List<Promote> promotes = new ArrayList<>();
            List<Release> releases = new ArrayList<>();
            for (Load load : loads) {
                promote = promoteRepository.findByLoad(load);
                release = releaseRepository.findByLoad(load);
                promotes.add(promote);
                releases.add(release);
            }

            List<ResultDto> results = new ArrayList<>();

            if (promote != null) {
                for (Promote prom : promotes) {
                    ResultDto resultDto = new ResultDto();
                    resultDto.setLoadName(prom.getLoad().getLoadName());
                    resultDto.setPromoteJobNum(prom.getJobNum());
                    resultDto.setPromoteStatus(prom.getStatus());
                    resultDto.setPromoteDate(prom.getDate());
                    results.add(resultDto);
                }

            }
            if (release != null) {
                for (Release rel : releases) {
                    ResultDto resultDto = new ResultDto();
                    resultDto.setReleaseDate(rel.getDate());
                    resultDto.setReleaseStatus(rel.getStatus());
                    resultDto.setReleaseToAuth(rel.getToAuth());
                    results.add(resultDto);
                }
            }

            for (ResultDto result : results) {
                String line = "<tr><td>" + result.getLoadName().trim() + "</td><td>" + result.getPromoteStatus().trim() + "</td><td>" + result.getPromoteJobNum().trim() + "</td><td>" + result.getPromoteDate().trim()
                        + "</td><td>" + result.getReleaseDate().trim() + "</td><td>" + result.getReleaseStatus().trim() + "</td><td>" + result.getReleaseToAuth().trim() + "</td></tr>";
                res.append(line);
            }
            String title = "فرم شماره " + formNum;
            jiraUtils.addPageAndContent(/*"<h2>" + formNum + "</h2> " +*/  res + "</tbody></table><br/><hr/>", title);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
