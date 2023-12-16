package com.example.hosptial_service.payloads;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Consent {
    @Id
    private String requestId;
    private Integer doctorId;
    private String patientId;
    private String requestingHospitalId;
    private String status;
    private String dateOfRequest;
    private String reqStartDate;
    private String reqEndDate;
    private String reqValidity;
    private String consentStartDate;
    private String consentEndDate;
    private String consentValidity;
    private String record_type;
    private Integer severity;


    public Consent() {
    }

    public Consent(Integer doctorId, String patientId, String requestingHospitalId, String reqStartDate, String reqEndDate, String reqValidity, String record_type, Integer severity) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.reqStartDate = reqStartDate;
        this.reqEndDate = reqEndDate;
        this.reqValidity = reqValidity;
        this.requestingHospitalId = requestingHospitalId;
        this.record_type = record_type;
        this.severity = severity;
    }

    public Consent(Integer doctorId, String patientId,String requestingHospitalId, String status, String reqStartDate, String reqEndDate, String reqValidity, String record_type, Integer severity) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.requestingHospitalId = requestingHospitalId;
        this.status = status;
        this.reqStartDate = reqStartDate;
        this.reqEndDate = reqEndDate;
        this.reqValidity = reqValidity;
        this.record_type = record_type;
        this.severity = severity;
    }
}
