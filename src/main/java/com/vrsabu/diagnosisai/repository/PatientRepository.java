package com.vrsabu.diagnosisai.repository;

import com.vrsabu.diagnosisai.entity.patientEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientRepository extends MongoRepository<patientEntity, ObjectId> {
    patientEntity findByPatientId(String patientId);
}
