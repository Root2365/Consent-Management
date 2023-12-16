package com.example.hospital_manager.controller;

import com.example.hospital_manager.auth.AuthenticationResponse;
import com.example.hospital_manager.entity.HospitalAddr;
import com.example.hospital_manager.payload.PatientRecord;
import com.example.hospital_manager.repo.HospitalAddrRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/patient-records")
public class PatientRecordsController{
    @Autowired
    private HospitalAddrRepo hospitalAddrRepo;
    @Autowired private WebClient webClient;
    @Value("${credentials.service-name}")
    private String serviceName;
    @Value("${credentials.password}")
    private String password;
    Map<String,String>hospital_auth;
    @PostConstruct
    public void init() {
        hospital_auth = new HashMap<>();
        hospital_auth.put("email",serviceName);
        hospital_auth.put("password",password);
    }
    @GetMapping("/get-records-hospital")
    ResponseEntity<?> getRecords(@RequestParam("patient_id") String patientId, @RequestParam("hospital_id") String hos_id){
        HospitalAddr h= hospitalAddrRepo.findById(hos_id).orElseThrow();
        String port = h.getAddr();
        HashMap<String,String> mt = new HashMap<>();
        AuthenticationResponse rt = webClient.post().uri("http://"+"localhost:"+port+"/api/v1/auth/authenticate").bodyValue(hospital_auth).retrieve().bodyToMono(AuthenticationResponse.class).block();
        String hospital_token ="Bearer "+rt.getToken();
        List<PatientRecord> pr_list = webClient.get().uri("http://localhost:"+port+"/api/v1/hospital-records/find_all?patient_id="+ patientId).header(HttpHeaders.AUTHORIZATION, hospital_token).retrieve().bodyToFlux(PatientRecord.class).collectList().block();
        return ResponseEntity.accepted().body(pr_list);
    }
}
