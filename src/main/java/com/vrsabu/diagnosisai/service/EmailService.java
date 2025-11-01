package com.vrsabu.diagnosisai.service;

import com.vrsabu.diagnosisai.entity.patientEntity;
import com.vrsabu.diagnosisai.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PatientRepository patientRepository;

    public void sendEmail(String patientId, String body) {
        patientEntity patient = patientRepository.findByPatientId(patientId);
        String emailId = patient.getEmail();
        try{
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo("bt23csh028@iiitn.ac.in");
            mail.setSubject("Report Generated");
            mail.setText(body);
            mailSender.send(mail);
        }catch (Exception e){
            log.error("Error sending email", e);
        }
    }
}
