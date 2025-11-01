package com.vrsabu.diagnosisai.service;

import com.vrsabu.diagnosisai.DTO.GetAllUserResponse;
import com.vrsabu.diagnosisai.entity.patientEntity;
import com.vrsabu.diagnosisai.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private static final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private PatientRepository patientRepository;

    public patientEntity createPatient(patientEntity patient) throws Exception {

        patient.setPatientId(generatePatientId());
        try {
            return patientRepository.save(patient);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static String generatePatientId() {
        long timestamp = System.currentTimeMillis();

        String timeHex = Long.toHexString(timestamp);

        byte[] randomBytes = new byte[6];
        secureRandom.nextBytes(randomBytes);

        StringBuilder randomHex = new StringBuilder();
        for (byte b : randomBytes) {
            randomHex.append(String.format("%02x", b));
        }

        return timeHex + randomHex;
    }

    public List<GetAllUserResponse> getAllPatients() throws Exception{
        try {
            List<patientEntity> patient = patientRepository.findAll();
            List<GetAllUserResponse> response = new ArrayList<>();
            for (patientEntity p : patient){
                GetAllUserResponse userResponse = new GetAllUserResponse();
                userResponse.setId(p.getPatientId());
                userResponse.setName(p.getName());
                userResponse.setAge(p.getAge());
                userResponse.setGender(p.getGender());
                userResponse.setEmail(p.getEmail());
                response.add(userResponse);
            }
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePatientDocumentTexts(String patientId, String cc, String hpi, String ref, String lab, String ot) {
        patientEntity patient = patientRepository.findByPatientId(patientId);
        if (patient != null) {
            patient.setChiefComplaint(cc);
            patient.setPreviousMedicalHistory(hpi);
            patient.setRelevantExamFindings(ref);
            patient.setLabInvestigation(lab);
            patient.setOngoingTreatments(ot);
            patientRepository.save(patient);
        }
    }
}
