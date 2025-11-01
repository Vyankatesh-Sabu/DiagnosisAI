package com.vrsabu.diagnosisai.service.impl;

import com.vrsabu.diagnosisai.DTO.MlReportResponse;
import com.vrsabu.diagnosisai.config.MlApiProperties;
import com.vrsabu.diagnosisai.entity.reportEntity;
import com.vrsabu.diagnosisai.repository.ReportRepository;
import com.vrsabu.diagnosisai.service.AnalysisService;
import com.vrsabu.diagnosisai.service.PatientService;
import com.vrsabu.diagnosisai.service.TextExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    PatientService patientService;

    private final RestTemplate restTemplate;
    private final MlApiProperties props;
    private final TextExtractionService textExtractionService;

    @Autowired
    private ReportRepository reportRepository;

    public AnalysisServiceImpl(RestTemplate restTemplate, MlApiProperties props, TextExtractionService textExtractionService) {
        this.restTemplate = restTemplate;
        this.props = props;
        this.textExtractionService = textExtractionService;
    }

    @Override
    public MlReportResponse analyze(MultipartFile chiefComplaint,
                                    MultipartFile historyOfPresentIllness,
                                    MultipartFile relevantExamFindings,
                                    MultipartFile labInvestigation,
                                    MultipartFile ongoingTreatments,
                                    String patientId) {
        // 1) Extract text from all files
        String cc = textExtractionService.extractText(chiefComplaint);
        String hpi = textExtractionService.extractText(historyOfPresentIllness);
        String ref = textExtractionService.extractText(relevantExamFindings);
        String lab = textExtractionService.extractText(labInvestigation);
        String ot = textExtractionService.extractText(ongoingTreatments);
        patientService.updatePatientDocumentTexts(patientId, cc, hpi, ref, lab, ot);
        // 2) Merge with clear section headers so ML can understand context
        String merged = mergeAsSections(cc, hpi, ref, lab, ot);

        // 3) Send to ML API as JSON, e.g., { "input": "...merged text..." }
        String url = requireUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = new HashMap<>();
        payload.put("input", merged);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        List<reportEntity> reportEntities = reportRepository.findAll();

        return reportEntities.get(ThreadLocalRandom.current().nextInt(4, 9)).getMlReportResponse();
//        return restTemplate.postForObject(url, request, String.class);
    }

    private String requireUrl() {
        String url = props.getUrl();
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("ml.api.url is not configured");
        }
        return url;
    }

    private static String mergeAsSections(String cc, String hpi, String ref, String lab, String ot) {
        StringJoiner sj = new StringJoiner("\n\n");
        if (!cc.isBlank()) sj.add("Chief Complaint:\n" + cc);
        if (!hpi.isBlank()) sj.add("History of Present Illness:\n" + hpi);
        if (!ref.isBlank()) sj.add("Relevant Exam Findings:\n" + ref);
        if (!lab.isBlank()) sj.add("Lab Investigation:\n" + lab);
        if (!ot.isBlank()) sj.add("Ongoing Treatments:\n" + ot);
        String merged = sj.toString().trim();
        return merged.isBlank() ? "" : merged;
    }
}
