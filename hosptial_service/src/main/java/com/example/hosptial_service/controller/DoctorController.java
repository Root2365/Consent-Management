package com.example.hosptial_service.controller;

import com.example.hosptial_service.auth.AuthenticationResponse;
import com.example.hosptial_service.entity.Doctor;
import com.example.hosptial_service.entity.PatientRecord;
import com.example.hosptial_service.exceptions.UserNotFoundException;
import com.example.hosptial_service.payloads.Consent;
import com.example.hosptial_service.payloads.HospitalAddrRequest;
import com.example.hosptial_service.repo.DoctorRepo;
import com.example.hosptial_service.service.DoctorService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/hospital-doctor")
public class DoctorController {
    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired private WebClient webClient;
    @Value("${credentials.id}")
    private String hospital_id;
    @Value("${credentials.name}")
    private String hospital_name;
    @Value("${credentials.password}")
    private String hospital_password;
    @Value("${server.port}")
    private String hospital_addr;

    private String hospital_manager = "http://localhost:9001/hospital-addr";
    private HashMap<String, String> convert(String res) {
        HashMap<String, String> map = new HashMap<>();
        map.put("response", res);
        return map;
    }
    Map<String,String> auth;
    @PostConstruct
    public void init() {
        auth = new HashMap<>();
        auth.put("password",hospital_password);
        auth.put("id",hospital_id);
    }
    private String getToken(){
        AuthenticationResponse resp = webClient.post().uri(hospital_manager+"/api/v1/auth/authenticate").bodyValue(auth).retrieve().bodyToMono(AuthenticationResponse.class).block();
        String token = resp.getToken();
        return "Bearer " + token;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-details")
    public ResponseEntity<?>hello(@AuthenticationPrincipal Doctor doctor){
        return ResponseEntity.accepted().body(doctor);
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create-consent")
    public ResponseEntity<?>create_consent(@AuthenticationPrincipal Doctor doctor,@RequestBody Consent consent){
        consent.setDoctorId(doctor.getId());
        String token = getToken();
        System.out.println("token in create consent: "+token);
        webClient.post().uri(hospital_manager+"/create-consent").bodyValue(consent).header(HttpHeaders.AUTHORIZATION, token).retrieve().bodyToMono(Consent.class).block();
        String response = "saved";
        return ResponseEntity.accepted().body(convert(response));
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping ("/get-consent")
    public ResponseEntity<?>get_all_consents(@AuthenticationPrincipal Doctor doctor) {
        Integer id = doctor.getId();
        String token = getToken();
        List<Consent>s_list = webClient.get().uri(hospital_manager+"/doctor/get-consents/"+id+"/"+hospital_id).header(HttpHeaders.AUTHORIZATION, token).retrieve().bodyToFlux(Consent.class).collectList().block();
        return ResponseEntity.accepted().body(s_list);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping ("/get-patient-data")
    public ResponseEntity<?>get_patient_data(@AuthenticationPrincipal Doctor doctor,@RequestParam("consent_id") String consent_id) {
        String token = getToken();
        List<PatientRecord>pr_list = webClient.get().uri(hospital_manager+"/get-patient-records/"+consent_id).header(HttpHeaders.AUTHORIZATION, token).retrieve().bodyToFlux(PatientRecord.class).collectList().block();
        return ResponseEntity.accepted().body(pr_list);
    }
}
