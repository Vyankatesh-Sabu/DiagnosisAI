package com.vrsabu.diagnosisai.repository;

import com.vrsabu.diagnosisai.entity.reportEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportRepository extends MongoRepository<reportEntity, String> {
    List<reportEntity> findByPatientId(String patientId);
}
