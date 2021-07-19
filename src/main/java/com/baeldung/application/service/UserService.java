package com.baeldung.application.service;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public synchronized Integer userAuthorized(String user, String pass) {

        String server = "192.168.180.15";
        FTPClient ftp = new FTPClient();
        int response = 200;
        try {
            if (user == null | pass == null | server == null) {
                response = 430;
            } else {

                ftp.connect(server);
                System.out.println(ftp.getReplyCode());
                if (ftp.getReplyCode() != 230 && ftp.getReplyCode() != 220)
                    response = 480;

                // login using user name and password
                ftp.login(user, pass);
                System.out.println(ftp.getReplyCode());
                if (ftp.getReplyCode() != 230 && ftp.getReplyCode() != 220)
                    response = 450;
            }
        } catch (Exception e) {
            e.getMessage();
            response = 480;

        }
        return response;
    }
}
