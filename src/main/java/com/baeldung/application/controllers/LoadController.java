package com.baeldung.application.controllers;

import com.baeldung.application.entities.Load;
import com.baeldung.application.repositories.LoadRepository;
import com.baeldung.application.service.LoadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class LoadController {
    private LoadRepository loadRepository;
    private LoadService service;
    @Value("E:\\Demo\\upload")
    String pathFile;

    public LoadController(LoadRepository loadRepository, LoadService service) {
        this.loadRepository = loadRepository;
        this.service = service;
    }

    @GetMapping("/loads")
    public List<Load> getLoads() {
        return (List<Load>) loadRepository.findAll();
    }

    @GetMapping("/{issueKey}")
    public List<Load> getLoadsByIssueKey(@RequestParam String issueKey) {
        return loadRepository.findByIssueKey(issueKey);
    }

    @PostMapping("/uploadFile/{source}/{bankName}/{issueKey}")
    public String saveLoads(@RequestParam("file") MultipartFile file, @RequestParam("source") String source
            , @RequestParam("bankName") String bankName, @RequestParam("issueKey") String issueKey) {
        try {
            source=source.toLowerCase().trim();
            bankName=bankName.toLowerCase().trim();
            issueKey=issueKey.toUpperCase().trim();
            byte[] bytes = file.getBytes();
            Path path = Paths.get((pathFile) + "\\" + file.getOriginalFilename());
            File f = new File(String.valueOf(Files.write(path, bytes)));
            service.saveLoad(f, source, bankName, issueKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getOriginalFilename();
    }

    @PostMapping("/loads")
    void addLoad(@RequestBody Load load) {

    }


}
