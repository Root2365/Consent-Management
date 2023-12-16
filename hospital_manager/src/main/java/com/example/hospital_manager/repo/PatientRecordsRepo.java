package com.example.hospital_manager.repo;

import com.example.hospital_manager.entity.PatientRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRecordsRepo extends JpaRepository<PatientRecords,Integer> {
    public boolean existsByHospitalIdAndPatientId(String hospitalId, String patientId);
    @Query(value = "SELECT hospital_id FROM patientrecords p WHERE p.patient_id = ?1",nativeQuery = true)
    public List<String>findByPatientId(String patientid);
}
