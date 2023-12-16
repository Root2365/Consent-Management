package com.consent.repo;

import com.consent.entity.Consent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ConsentRepo extends JpaRepository<Consent, String> {
    public ArrayList<Consent> findAllByPatientId(String patientId);
    //public ArrayList<Consent> findAllByDoctorIdAndSendingHospitalId(Integer doctorId, String hospitalId);
    public ArrayList<Consent> findAllByDoctorIdAndRequestingHospitalId(Integer doctorId, String hospitalId);
    public Consent findByRequestId(String consentId);
}
