package com.vrsabu.diagnosisai.web;

import com.vrsabu.diagnosisai.service.AnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {


    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping(path = "/diagnose", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> diagnose(
            @RequestPart("chief_complaint") MultipartFile chiefComplaint,
            @RequestPart("history_of_present_illness") MultipartFile historyOfPresentIllness,
            @RequestPart("relevant_exam_findings") MultipartFile relevantExamFindings,
            @RequestPart("lab_investigation") MultipartFile labInvestigation,
            @RequestPart("ongoing_treatments") MultipartFile ongoingTreatments
    ) {
        String result = analysisService.analyze(
                chiefComplaint,
                historyOfPresentIllness,
                relevantExamFindings,
                labInvestigation,
                ongoingTreatments
        );
        return new ResponseEntity<>(Map.of("Result", result), HttpStatus.OK);
//        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Analysis Service is up and running.");
    }
}
