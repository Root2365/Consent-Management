package com.example.hosptial_service.service;
import com.example.hosptial_service.entity.Doctor;

import java.util.Map;

public interface HospitalAdminService {
//    String newUser(Doctor user);
//    String login(String email, String password);
//    String logout(String email);
//    String AddDoctor(Doctor d);
//    void deleteUser(String email);
    void updateUserDetails(String email, Map<String, String> payload);
    boolean changePassword(String email, Map<String, String> payload);
    
}
