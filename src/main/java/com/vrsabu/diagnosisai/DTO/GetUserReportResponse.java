package com.vrsabu.diagnosisai.DTO;

import lombok.Data;

@Data
public class GetUserReportResponse {
    private String reportId;
    private String patientId;
    private MlReportResponse mlReportResponse;
}
