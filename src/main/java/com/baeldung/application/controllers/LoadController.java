package com.baeldung.application.controllers;

import com.baeldung.application.entities.Load;
import com.baeldung.application.service.LoadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class LoadController {
    private final LoadService service;
    @Value("E:\\Demo\\upload")
    String pathFile;

    public LoadController( LoadService service) {
        this.service = service;
    }

    @PostMapping("/uploadFile/{source}/{bankName}/{issueKey}/{formNumber}/{stateFlow}")
    public String saveLoads(@RequestParam("file") MultipartFile file, @PathVariable String source,
                            @PathVariable String bankName, @PathVariable String issueKey,
                            @PathVariable String formNumber,@PathVariable int stateFlow) {
        try {
            source=source.toLowerCase().trim();
            bankName=bankName.toLowerCase().trim();
            issueKey=issueKey.toUpperCase().trim();
            byte[] bytes = file.getBytes();
            Path path = Paths.get((pathFile) + "\\" + file.getOriginalFilename());
            File f = new File(String.valueOf(Files.write(path, bytes)));
            if(stateFlow==1) {
                service.saveLoad(f, source, bankName, issueKey);
            }else {
                List<Load> loadList = service.findLoads(f, formNumber);
                for (Load load : loadList) {
                    load.setReleaseTurn("1");
                    service.updateLoad(load);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getOriginalFilename();
    }

}
