package com.example.csv3.service;

import com.example.csv3.exception.CsvException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CsvService {

    List<Long> getDevsForCommonProject(MultipartFile file) throws IOException, CsvException;
}
