package com.example.hosptial_service.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.hosptial_service.auth.AuthenticationResponse;
import com.example.hosptial_service.auth.AuthenticationService;
import com.example.hosptial_service.auth.RegisterRequest;
import com.example.hosptial_service.entity.Doctor;
import com.example.hosptial_service.payloads.HospitalAddrRequest;
import com.example.hosptial_service.service.HospitalAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/hospital-admin")
@RequiredArgsConstructor
public class HospitalAdminController {
    @Autowired
    private HospitalAdminService hospitalAdminService;
    private HashMap<String, String> convert(String res) {
        HashMap<String, String> map = new HashMap<>();
        map.put("response", res);
        return map;
    }
    private final AuthenticationService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-doctor")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update-details")
    public ResponseEntity<?> update(
            @RequestParam String email,
            @RequestBody Map<String, String> payload
    ) {
        if(payload.containsKey("password") && hospitalAdminService.changePassword(email, payload)!=true){
            ResponseEntity.badRequest().body("Invalid password");
        }
        hospitalAdminService.updateUserDetails(email, payload);
        return ResponseEntity.ok().body(convert("Success"));
    }
}
