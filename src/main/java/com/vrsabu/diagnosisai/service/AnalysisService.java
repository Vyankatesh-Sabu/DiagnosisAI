package com.vrsabu.diagnosisai.service;

import com.vrsabu.diagnosisai.DTO.MlReportResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Contract for analyzing patient data using five uploaded files and returning a string result.
 */

public interface AnalysisService {
    MlReportResponse analyze(
            MultipartFile chiefComplaint,
            MultipartFile historyOfPresentIllness,
            MultipartFile relevantExamFindings,
            MultipartFile labInvestigation,
            MultipartFile ongoingTreatments,
            String patientId
    );
}
