package com.vrsabu.diagnosisai.service.impl;

import com.vrsabu.diagnosisai.service.TextExtractionService;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;

import java.io.InputStream;

@Service
public class TextExtractionServiceImpl implements TextExtractionService {
    private final AutoDetectParser parser = new AutoDetectParser();

    @Override
    public String extractText(MultipartFile file) {
        if (file == null || file.isEmpty()) return "";
        try (InputStream is = file.getInputStream()) {
            ContentHandler handler = new BodyContentHandler(-1); // unlimited
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();
            parser.parse(is, handler, metadata, context);
            return handler.toString().trim();
        } catch (Exception e) {
            // Fallback: try to read raw text
            try {
                return new String(file.getBytes());
            } catch (Exception ex) {
                throw new RuntimeException("Failed to extract text from file: " + file.getOriginalFilename(), e);
            }
        }
    }
}

