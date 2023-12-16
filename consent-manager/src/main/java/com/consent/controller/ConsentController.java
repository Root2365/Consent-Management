package com.consent.controller;

import com.consent.entity.Consent;
import com.consent.service.ConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/consent")
public class ConsentController {
    @Autowired
    private ConsentService consentService;

    private HashMap<String, String> convert(String res) {
        HashMap<String, String> map = new HashMap<>();
        map.put("response", res);
        return map;
    }

    @PostMapping("/doctor/create")
    public ResponseEntity<?> newRequestFromDoctor(@RequestBody Consent consent){
        String res = consentService.newConsent(consent);
        if(!res.matches("Success")){
            return ResponseEntity.badRequest().body(convert(res));
        }
        return ResponseEntity.ok(convert(res));
    }

    @GetMapping("/get/doctor")
    public ResponseEntity<?> getAllConsentsDoctor(@RequestParam("doctor_id") Integer doctorId, @RequestParam("hospital_id") String hospitalId) {
        ArrayList<Consent> consents = consentService.allConsentsDoctor(doctorId, hospitalId);
        if(consents==null){
            return ResponseEntity.badRequest().body("No consent requested by the doctor");
        }
        return ResponseEntity.ok(consents);
    }
    @GetMapping("/get/doctor/{doctor_id}/{hospital_id}")
    public ResponseEntity<?> getAllConsentsDoctor2(@PathVariable Integer doctor_id, @PathVariable String hospital_id) {
        ArrayList<Consent> consents = consentService.allConsentsDoctor(doctor_id,hospital_id);
        if(consents==null){
            return ResponseEntity.badRequest().body("No consent requested by the doctor");
        }
        return ResponseEntity.ok(consents);
    }


    @GetMapping("/patient/getall")
    public ResponseEntity<?> getAllConsentsPatient(@RequestParam("patientid") String patientId) {
        ArrayList<Consent> consents = consentService.allConsentsPatient(patientId);
        if(consents==null){
            return ResponseEntity.badRequest().body("No consent requested for the patient");
        }
        return ResponseEntity.ok(consents);
    }

    @PutMapping("/patient/update")
    public ResponseEntity<?> updateConsent(@RequestParam("consent_id") String consent_id, @RequestBody Map<String, String> payload){
        if(payload.get("status").matches("reject")){
            consentService.rejectConsent(consent_id);
            return ResponseEntity.ok(convert("changes made successfully"));
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            consentService.updateConsent(consent_id, f.parse(payload.get("startDate")), f.parse(payload.get("endDate")), f.parse(payload.get("validity")));
            return ResponseEntity.ok(convert("changes made successfully"));
        } catch (ParseException e){
            return ResponseEntity.ok("Cannot make the changes");
        }
    }

    @GetMapping("/get_consent")
    public ResponseEntity<?> getConsent(@RequestParam("consent_id") String consent_id){
        Consent consent = consentService.getConsent(consent_id);
        if(consent==null){
            return ResponseEntity.ok(convert("invalid consent id"));
        }
        return ResponseEntity.ok(consent);
    }
}
