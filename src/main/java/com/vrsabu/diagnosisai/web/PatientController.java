package com.vrsabu.diagnosisai.web;

import com.vrsabu.diagnosisai.DTO.CreateUserRequest;
import com.vrsabu.diagnosisai.DTO.GetAllUserResponse;
import com.vrsabu.diagnosisai.DTO.GetUserReportResponse;
import com.vrsabu.diagnosisai.entity.patientEntity;
import com.vrsabu.diagnosisai.repository.ReportRepository;
import com.vrsabu.diagnosisai.service.EmailService;
import com.vrsabu.diagnosisai.service.PatientService;
import com.vrsabu.diagnosisai.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/create")
    public ResponseEntity<?> createPatient(@RequestBody CreateUserRequest createUserRequest){
        if(createUserRequest.getAge() < 0){
            return new ResponseEntity<>("Age is invalid", HttpStatus.BAD_REQUEST);
        }
        if(createUserRequest.getGender() == null  || createUserRequest.getName() == null){
            return new ResponseEntity<>("Gender or Name is missing", HttpStatus.BAD_REQUEST);
        }
        patientEntity patient = new patientEntity();
        patient.setAge(createUserRequest.getAge());
        patient.setGender(createUserRequest.getGender());
        patient.setName(createUserRequest.getName());
        patient.setEmail(createUserRequest.getEmail());
        try{
            return new ResponseEntity<>(patientService.createPatient(patient), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>("Error creating patient: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<List<GetAllUserResponse>> getAllPatients() {
        try{
            List<GetAllUserResponse> getAllUserResponses= patientService.getAllPatients();
            return new ResponseEntity<>(getAllUserResponses, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientReportInfo(@PathVariable("id") String id){
        try{
            List<GetUserReportResponse> getUserReportResponses = reportService.getUserReport(id);
            Map<String,List<GetUserReportResponse>> res = Map.of("reports", getUserReportResponses);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Error retrieving patient: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/send-report")
    public ResponseEntity<?> sendPatientReport(){
        emailService.sendEmail("19a3f65948d9ce666fc1932", "hello email is sent");
        return ResponseEntity.ok().build();
    }
}
