package com.example.hosptial_service.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hosptial_service.auth.AuthenticationResponse;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.hosptial_service.entity.PatientRecord;
import com.example.hosptial_service.service.PatientRecordService;
import org.springframework.web.reactive.function.client.WebClient;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/hospital-records")
public class PatientRecordController {
    @Autowired
    public PatientRecordService patientRecordService;

    @Autowired private WebClient webClient;

    @Value("${credentials.id}")
    private String hospital_id;
    @Value("${credentials.name}")
    private String hospital_name;
    @Value("${credentials.password}")
    private String hospital_password;

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
    @PostMapping("/add")
    public ResponseEntity<?> savePatientRecord(@RequestBody PatientRecord patientRecord){

         String response = patientRecordService.newUser(patientRecord);
         String token = getToken();
         String res = webClient.post().uri(hospital_manager + "/add-patient-hospital?patient-id=" + patientRecord.getPatientId()).header(HttpHeaders.AUTHORIZATION, token).retrieve().bodyToMono(String.class).block();
         System.out.println(res);
         return ResponseEntity.accepted().body(convert(response));
    }
    @GetMapping("/find_all")
    public List<PatientRecord> findByPatientId(@QueryParam("patient_id") String patient_id){
        return  patientRecordService.findByPatientId(patient_id);

    }

//    @GetMapping("/find_all/{from}/{to}/{patient_id}")
//    public List<PatientRecord> findByDateOfVisitAndPatientId(@PathVariable("from") String d1,@PathVariable("to") String d2,@PathVariable("patient_id") String patient_id) throws ParseException{
//        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
//        Date date1=f.parse(d1);
//        Date date2=f.parse(d2);
//        return patientRecordService.findByDateOfVisitAndPatientId(date1, date2, patient_id);
//    }
    @GetMapping("/search_all")
    public List<PatientRecord> searchAll(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate, @QueryParam("patient_id") String patient_id, @QueryParam("record_type") String record_type, @QueryParam("severity") Integer severity) throws ParseException{
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
        Date date1=f.parse(startDate);
        Date date2=f.parse(endDate);
        return patientRecordService.findByCertainParams(date1,date2,patient_id,record_type,severity);
    }

}
