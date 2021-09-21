package com.baeldung.application.controllers;

import com.baeldung.application.service.PromoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromoteController {
    private final PromoteService service;

    public PromoteController(PromoteService service) {
        this.service = service;
            }

    @GetMapping("/promotes/{issueKey}")
    public void getPromote( @PathVariable String issueKey) {
        issueKey = issueKey.toUpperCase().trim();
        service.promote(issueKey);
    }

    @GetMapping("/result/{issueKey}/{formNum}")
    public void sendResult( @PathVariable String issueKey, @PathVariable String formNum) {
          service.promoteResult(issueKey,formNum);
    }
}
