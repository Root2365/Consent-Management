package com.example.hosptial_service.service;

import com.example.hosptial_service.entity.Doctor;
import com.example.hosptial_service.entity.PatientRecord;
import com.example.hosptial_service.exceptions.UserNotFoundException;
import com.example.hosptial_service.repo.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepo doctorRepo;
    public String SaveUser(Doctor user) {
        doctorRepo.save(user);
        return "saved";
    }
    public Doctor validateAndGetUserById(Integer id) {
        return doctorRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username not found")));
    }



}
