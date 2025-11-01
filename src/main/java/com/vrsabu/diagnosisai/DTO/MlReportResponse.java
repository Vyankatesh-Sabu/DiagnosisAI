package com.vrsabu.diagnosisai.DTO;

import lombok.Data;

import java.util.List;

@Data
public class MlReportResponse {
    private String summary;
    private DifferentialDiagnosis differential_diagnosis;
    private List<String> references;
}

@Data
class DifferentialDiagnosis {
    private int rank;
    private String diagnosis;
    private String justification;
}
