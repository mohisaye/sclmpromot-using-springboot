package com.baeldung.application.service;

import com.baeldung.application.entities.Issue;
import com.baeldung.application.entities.User;
import com.baeldung.application.repositories.UserRepository;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    User user;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Issue issue;

    public synchronized Integer userAuthorized(String username, String pass, String issueKey, String formNumber) {
        String server = "192.168.180.15";
        FTPClient ftp = new FTPClient();
        int response = 200;
        try {
            if (user == null | pass == null | server == null) {
                response = 430;
            } else {

                ftp.connect(server);
                System.out.println(ftp.getReplyCode());
                if (ftp.getReplyCode() == 230 || ftp.getReplyCode() == 220) {
                    // login using user name and password
                    ftp.login(username, pass);
                    System.out.println(ftp.getReplyCode());
                    System.out.println(ftp.getReplyString());
                    if (ftp.getReplyCode() == 230 || ftp.getReplyCode() == 220) {
                        saveUserInfo(username,pass, formNumber, issueKey);
                    } else {
                        response = 450;
                    }
                } else {
                    response = 480;
                }
            }
        } catch (Exception e) {
            e.getMessage();
            response = 480;
        }
        return response;
    }

    public void saveUserInfo(String userName,String password, String formNumber, String issueKey) {
        Issue issue=new Issue();
        User user=new User();
        issue.setFormNumber(formNumber);
        issue.setIssueKey(issueKey);
        user.setUserName(userName);
        user.setPassword(password);
        user.setIssue(issue);
        userRepository.save(user);
    }
}
