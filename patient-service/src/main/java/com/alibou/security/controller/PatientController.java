package com.alibou.security.controller;

import com.alibou.security.auth.AuthenticationResponse;
import com.alibou.security.entity.Consent;
import com.alibou.security.entity.Patient;
import com.alibou.security.entity.PatientRecord;
import com.alibou.security.repository.PatientRepository;
import com.alibou.security.service.PatientService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {
  private String consentServer = "http://localhost:9002/consent/";
  @Value("${hospital-manager.address}")
  private String hospitalManager;
  @Autowired
  private PatientService patientService;
  @Autowired
  private WebClient webClient;
  @Value("${credentials.service-name}")
  private String serviceName;
  @Value("${credentials.password}")
  private String password;
  @Value("${credentials.id}")
  private String service_id;
  Map<String,String>auth;
  Map<String,String>hospitalManagerAuth;

  @PostConstruct
  public void init() {
    auth = new HashMap<>();
    auth.put("serviceName", serviceName);
    auth.put("password", password);
    hospitalManagerAuth = new HashMap<>();
    hospitalManagerAuth.put("id",service_id);
    hospitalManagerAuth.put("password",password);
  }
    @GetMapping("/get-details")
  public ResponseEntity<?>hello(@AuthenticationPrincipal Patient patient) {
    return ResponseEntity.accepted().body(patient);
  }
  @GetMapping("/all-consents")
  ResponseEntity<?> getAllConsents(@AuthenticationPrincipal Patient patient){
    String patientId = patient.getId();
    AuthenticationResponse resp = webClient.post().uri(consentServer+"api/v1/auth/authenticate").bodyValue(auth).retrieve().bodyToMono(AuthenticationResponse.class).block();
    String token = resp.getToken();
    List<Consent> consent_list = webClient.get().uri(consentServer + "patient/getall?patientid=" + patientId).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).retrieve().bodyToFlux(Consent.class).collectList().block();
    return ResponseEntity.ok(consent_list);
  }

  @PutMapping("/update-consent")
  ResponseEntity<?> updateConsents(@RequestParam("consent_id") String consent_id, @RequestBody Map<String, String> payload){
    AuthenticationResponse resp = webClient.post().uri(consentServer+"/api/v1/auth/authenticate").bodyValue(auth).retrieve().bodyToMono(AuthenticationResponse.class).block();
    String token = resp.getToken();
    String response = webClient.put().uri(consentServer+"patient/update?consent_id=" + consent_id).bodyValue(payload).header(HttpHeaders.AUTHORIZATION,"Bearer "+token).retrieve().bodyToMono(String.class).block();
    return ResponseEntity.accepted().body(response);
  }

  @GetMapping("/get-records")
  ResponseEntity<?> getRecords(@AuthenticationPrincipal Patient patient, @RequestParam("hospital_id") String hos_id){
    String patientId =  patient.getId();
    AuthenticationResponse resp = webClient.post().uri(hospitalManager+"hospital-addr/api/v1/auth/authenticate").bodyValue(hospitalManagerAuth).retrieve().bodyToMono(AuthenticationResponse.class).block();
    String token = resp.getToken();
    List<PatientRecord> pr_list = webClient.get().uri(hospitalManager + "api/v1/patient-records/get-records-hospital?patient_id="+ patientId + "&hospital_id=" +hos_id).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).retrieve().bodyToFlux(PatientRecord.class).collectList().block();
    return ResponseEntity.accepted().body(pr_list);
  }

}
