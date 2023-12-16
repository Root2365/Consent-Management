package com.example.hospital_manager.payload;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class Consent {
    @Id
    private String requestId;
    private Integer doctorId;
    private String patientId;
    private String requestingHospitalId;
    private String sendingHospitalId;
    private String status;
    private String dateOfRequest;
    private String reqStartDate;
    private String reqEndDate;
    private String reqValidity;
    @Temporal(TemporalType.DATE)

    private String consentStartDate;
    @Temporal(TemporalType.DATE)

    private String consentEndDate;
    @Temporal(TemporalType.DATE)

    private Date consentValidity;
    private String record_type;
    private Integer severity;
    public Consent() {
    }

    public Consent(Integer doctorId, String patientId, String requestingHospitalId, String sendingHospitalId, String reqStartDate, String reqEndDate, String reqValidity, String record_type, Integer severity) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.sendingHospitalId = sendingHospitalId;
        this.reqStartDate = reqStartDate;
        this.reqEndDate = reqEndDate;
        this.reqValidity = reqValidity;
        this.requestingHospitalId = requestingHospitalId;
        this.record_type = record_type;
        this.severity = severity;
    }

    public Consent(Integer doctorId, String patientId,String requestingHospitalId, String sendingHospitalId, String status, String reqStartDate, String reqEndDate, String reqValidity, String record_type, Integer severity) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.requestingHospitalId = requestingHospitalId;
        this.sendingHospitalId = sendingHospitalId;
        this.status = status;
        this.reqStartDate = reqStartDate;
        this.reqEndDate = reqEndDate;
        this.reqValidity = reqValidity;
        this.record_type = record_type;
        this.severity = severity;
    }
}
