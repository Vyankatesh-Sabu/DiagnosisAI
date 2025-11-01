package com.vrsabu.diagnosisai.entity;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class patientEntity {
    @Id()
    private String id;
    @Indexed(unique = true)
    @NonNull
    private String patientId;
    @NonNull
    private String name;
    @NonNull
    private int age;
    @NonNull
    private String gender;
    @NonNull
    private String email;
    private String previousMedicalHistory;
    private String chiefComplaint;
    private String relevantExamFindings;
    private String labInvestigation;
    private String ongoingTreatments;
}
