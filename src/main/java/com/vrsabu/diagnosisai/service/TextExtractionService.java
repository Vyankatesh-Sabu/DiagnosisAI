package com.vrsabu.diagnosisai.service;

import org.springframework.web.multipart.MultipartFile;

public interface TextExtractionService {
    String extractText(MultipartFile file);
}

