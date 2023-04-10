package com.example.csv3.controller;

import com.example.csv3.exception.CsvException;
import com.example.csv3.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping(path = "/commonProject")
public class CsvController {

    private final CsvService csvService;

    @Autowired
    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

//    @GetMapping(value = "/commonProjectDevs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/devs")
    public ResponseEntity updateParkingSpaceUnit(@RequestParam("file") MultipartFile file) throws IOException, CsvException {
        try {
            return ResponseEntity.ok(csvService.getDevsForCommonProject(file));
        } catch (CsvException e) {
//            return ResponseEntity.badRequest().body(new CsvException(e.getErrorCode(), e.getMessage())).getBody();
            return ResponseEntity.ok(new CsvException(e.getErrorCode(), e.getMessage()));
        }
    }
}
