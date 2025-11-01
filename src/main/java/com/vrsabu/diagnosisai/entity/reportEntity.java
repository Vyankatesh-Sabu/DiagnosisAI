package com.vrsabu.diagnosisai.entity;

import com.vrsabu.diagnosisai.DTO.MlReportResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class reportEntity {
    @Id
    private String id;
    @Indexed(unique = false)
    private String patientId;
    private MlReportResponse mlReportResponse;
}
