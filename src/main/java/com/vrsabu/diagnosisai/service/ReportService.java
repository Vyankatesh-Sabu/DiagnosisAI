package com.vrsabu.diagnosisai.service;

import com.vrsabu.diagnosisai.DTO.GetUserReportResponse;
import com.vrsabu.diagnosisai.entity.reportEntity;
import com.vrsabu.diagnosisai.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public List<GetUserReportResponse> getUserReport(String patientId) {
        List<reportEntity> report = reportRepository.findByPatientId(patientId);
        List<GetUserReportResponse> userReports = new ArrayList<>();
        for (reportEntity re : report) {
            GetUserReportResponse response = new GetUserReportResponse();
            response.setReportId(re.getId());
            response.setPatientId(re.getPatientId());
            response.setMlReportResponse(re.getMlReportResponse());
            userReports.add(response);
        }
        return userReports;
    }
}
