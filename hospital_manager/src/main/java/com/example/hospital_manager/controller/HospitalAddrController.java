package com.example.hospital_manager.controller;

import com.example.hospital_manager.auth.AuthenticationResponse;
import com.example.hospital_manager.entity.HospitalAddr;
import com.example.hospital_manager.entity.PatientRecords;
import com.example.hospital_manager.payload.Consent;
import com.example.hospital_manager.payload.HospitalAddrRequest;
import com.example.hospital_manager.payload.PatientRecord;
import com.example.hospital_manager.repo.HospitalAddrRepo;
import com.example.hospital_manager.repo.PatientRecordsRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/hospital-addr")
public class HospitalAddrController {
    @Autowired
    private HospitalAddrRepo hospitalAddrRepo;

    @Autowired
    private PatientRecordsRepo patientRecordsRepo;
    @Autowired private WebClient webClient;
    private String consentServer = "http://localhost:9002/consent/";
    @Value("${credentials.service-name}")
    private String serviceName;
    @Value("${credentials.password}")
    private String password;
    Map<String,String> auth;
    Map<String,String>hospital_auth;
    @PostConstruct
    public void init() {
        auth = new HashMap<>();
        auth.put("serviceName", serviceName);
        auth.put("password", password);
        hospital_auth = new HashMap<>();
        hospital_auth.put("email",serviceName);
        hospital_auth.put("password",password);
    }
    private HashMap<String, String> convert(String res) {
        HashMap<String, String> map = new HashMap<>();
        map.put("response", res);
        return map;
    }

    private String getToken(){
        AuthenticationResponse resp = webClient.post().uri(consentServer+"api/v1/auth/authenticate").bodyValue(auth).retrieve().bodyToMono(AuthenticationResponse.class).block();
        String token = resp.getToken();
        return "Bearer " + token;
    }
    @PostMapping("/add-hospital")
    public ResponseEntity<?> add(@RequestBody HospitalAddrRequest hospitalAddrRequest){
        var addr = HospitalAddr.builder()
                .addr(hospitalAddrRequest.getAddr())
                .id(hospitalAddrRequest.getId())
                .name(hospitalAddrRequest.getName())
                .build();
        hospitalAddrRepo.save(addr);
        String response = "saved";
        return ResponseEntity.accepted().body(addr);
    }
    @GetMapping("/doctor/get-consents/{doctor_id}/{hospital_id}")
    public ResponseEntity<?>get_consents_doctor(@PathVariable Integer doctor_id,@PathVariable String hospital_id){
        String token = getToken();
        List<Consent>s_list = webClient.get().uri(consentServer+"get/doctor?doctor_id="+doctor_id+"&hospital_id="+hospital_id).header(HttpHeaders.AUTHORIZATION,token ).retrieve().bodyToFlux(Consent.class).collectList().block();
        return ResponseEntity.accepted().body(s_list);
    }
    @PostMapping("/create-consent")
   public ResponseEntity<?>post_consent(@RequestBody Consent consent){
        String response = "forwarded";
        String token = getToken();
        webClient.post().uri(consentServer+"doctor/create").bodyValue(consent).header(HttpHeaders.AUTHORIZATION, token).retrieve().bodyToMono(Consent.class).block();
        return ResponseEntity.accepted().body(convert(response));
    }
    @GetMapping("/get-patient-records/{consent_id}")
    public ResponseEntity<?>get_patient_records(@PathVariable String consent_id){
        String token = getToken();
        Consent consent = webClient.get().uri("http://localhost:9002/consent/get_consent?consent_id="+consent_id).header(HttpHeaders.AUTHORIZATION, token).retrieve().bodyToMono(Consent.class).block();
        List<PatientRecord> pr_list  =new ArrayList<>();
        // Get calender
        Date now = new Date();
        if(consent==null)
            return ResponseEntity.accepted().body(pr_list);

        String status = consent.getStatus();
        if(status.equalsIgnoreCase("emergency")){
            consent.setConsentStartDate(consent.getReqStartDate());
            consent.setConsentEndDate(consent.getReqEndDate());
        }
        String startDate = consent.getConsentStartDate();
        String endDate =consent.getConsentEndDate();

        if(!status.equalsIgnoreCase("accepted") && !status.equalsIgnoreCase("emergency")) {
            PatientRecord p = new PatientRecord();
            p.setReportDetails("Consent not given to view data");
            p.setPatientId(consent.getPatientId());
            pr_list.add(p);
            return ResponseEntity.accepted().body(pr_list);
        }
        if(now.after(consent.getConsentValidity())){
            PatientRecord p = new PatientRecord();
            p.setReportDetails("Consent Expired");
            p.setPatientId(consent.getPatientId());
            pr_list.add(p);
            return ResponseEntity.accepted().body(pr_list);
        }
        String patient_id = consent.getPatientId();
        List<String> hospitalIds =patientRecordsRepo.findByPatientId(patient_id);


        for(String hospital:hospitalIds){
            HospitalAddr h= hospitalAddrRepo.findById(hospital).orElseThrow();
            String port = h.getAddr();
            List<PatientRecord> temp_list  =new ArrayList<>();
            AuthenticationResponse rt = webClient.post().uri("http://"+"localhost:"+port+"/api/v1/auth/authenticate").bodyValue(hospital_auth).retrieve().bodyToMono(AuthenticationResponse.class).block();
            String hospital_token ="Bearer "+rt.getToken();
            temp_list = webClient.get().uri(uriBuilder -> uriBuilder.scheme("http").host("localhost").port(port).path("/api/v1/hospital-records/search_all").queryParam("startDate", startDate.toString()).queryParam("endDate", endDate.toString()).queryParam("patient_id", patient_id).queryParam("record_type", consent.getRecord_type()).queryParam("severity", consent.getSeverity()).build()).header(HttpHeaders.AUTHORIZATION, hospital_token).retrieve().bodyToFlux(PatientRecord.class).collectList().block();
            pr_list.addAll(temp_list);
        }
        return ResponseEntity.accepted().body(pr_list);
    }

    @PostMapping("/add-patient-hospital")
    public ResponseEntity<?> addPatientHospital(@AuthenticationPrincipal HospitalAddr hospitalAddr, @RequestParam("patient-id") String patient_id){
        System.out.println(patient_id);
        PatientRecords patientRecords = new PatientRecords( patient_id, hospitalAddr.getId());
        if(!patientRecordsRepo.existsByHospitalIdAndPatientId(hospitalAddr.getId(), patient_id)){
            patientRecordsRepo.save(patientRecords);
            System.out.println("HELLO");
        }else{
            System.out.println("HELLO BYE");

        }
        System.out.println(hospitalAddr.getId() + " " + patient_id);
        return ResponseEntity.ok("Success");
    }
}
