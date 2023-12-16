package com.example.hosptial_service.service;

import java.util.Date;
import java.util.List;

import com.example.hosptial_service.entity.PatientRecord;

public interface PatientRecordService {
    String newUser(PatientRecord user);
    List<PatientRecord> findByPatientId(String id);
    List<PatientRecord>findByDateOfVisitAndPatientId(Date d1,Date d2,String patient_id);
    List<PatientRecord>findByCertainParams(Date d1,Date d2,String patient_id,String record_type,Integer severity);
    

}
