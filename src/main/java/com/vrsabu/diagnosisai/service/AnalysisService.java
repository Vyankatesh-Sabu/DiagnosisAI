package com.vrsabu.diagnosisai.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Contract for analyzing patient data using five uploaded files and returning a string result.
 */

public interface AnalysisService {
    String analyze(
            MultipartFile chiefComplaint,
            MultipartFile historyOfPresentIllness,
            MultipartFile relevantExamFindings,
            MultipartFile labInvestigation,
            MultipartFile ongoingTreatments
    );
}
