package com.baeldung.application.controllers;

import com.baeldung.application.service.ReleaseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReleaseController {
    private final ReleaseService service;

    @Value("E:\\Demo\\uploadRelease")
    String pathFile;

    public ReleaseController(ReleaseService service) {
        this.service = service;
    }


    @GetMapping("/release/{toAuth}/{formNumber}")
    public ResponseEntity<String> addRlease(@PathVariable String formNumber, @PathVariable String toAuth) {
        int response = 0;
        try {
            response = service.release(toAuth, formNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != 0) {
            return new ResponseEntity<>(
                    String.valueOf(response),
                    HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
