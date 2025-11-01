package com.vrsabu.diagnosisai.web;

import com.vrsabu.diagnosisai.DTO.MlReportResponse;
import com.vrsabu.diagnosisai.entity.reportEntity;
import com.vrsabu.diagnosisai.repository.ReportRepository;
import com.vrsabu.diagnosisai.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @PostMapping("/report/{id}")
    public String createReport(
            @RequestBody MlReportResponse response,
            @PathVariable String id
            ){
        reportEntity merged = new reportEntity();
        merged.setPatientId(id);
        merged.setMlReportResponse(response);
        reportRepository.save(merged);
        return "Report saved successfully";
    }
}
