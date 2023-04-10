package com.example.csv3.validation;

import com.example.csv3.exception.CsvException;
import com.example.csv3.exception.codes.Codes;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class CsvValidator {

    public void validateFileType(MultipartFile file) throws CsvException {
        String fileName = file.getOriginalFilename().toUpperCase();
        if(!fileName.endsWith(".CSV")) {
            throw new CsvException(Codes.WRONG_FILE_TYPE);
        }
    }
}
