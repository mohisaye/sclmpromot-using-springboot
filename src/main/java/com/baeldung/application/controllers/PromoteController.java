package com.baeldung.application.controllers;

import com.baeldung.application.service.PromoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromoteController {
    @Autowired
    private final PromoteService service;

    public PromoteController(PromoteService service) {
        this.service = service;
    }

    @GetMapping("/promotes/{user}/{pass}/{issueKey}")
    public void getPromote(@RequestParam String user, @RequestParam String pass, @RequestParam String issueKey) {
        user = user.toUpperCase().trim();
        issueKey = issueKey.toUpperCase().trim();
        service.promote(user, pass, issueKey);
    }

    @GetMapping("/promotes/result/{issueKey}/{formNum}")
    public void sendResult(@RequestParam String issueKey, @RequestParam String formNum) {
          service.promoteResult(issueKey,formNum);
    }
}
