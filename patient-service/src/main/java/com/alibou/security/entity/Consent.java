package com.alibou.security.entity;
import jakarta.persistence.Id;
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
    private String sendingHospitalId;
    private String status;
    private String dateOfRequest;
    private String reqStartDate;
    private String reqEndDate;
    private String reqValidity;
    private String consentStartDate;
    private String consentEndDate;
    private String consentValidity;

    public Consent() {
    }

    public Consent(Integer doctorId, String patientId, String requestingHospitalId, String sendingHospitalId, String reqStartDate, String reqEndDate, String reqValidity) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.sendingHospitalId = sendingHospitalId;
        this.reqStartDate = reqStartDate;
        this.reqEndDate = reqEndDate;
        this.reqValidity = reqValidity;
        this.requestingHospitalId = requestingHospitalId;
    }

    public Consent(Integer doctorId, String patientId, String requestingHospitalId, String sendingHospitalId, String status, String reqStartDate, String reqEndDate, String reqValidity) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.requestingHospitalId = requestingHospitalId;
        this.sendingHospitalId = sendingHospitalId;
        this.status = status;
        this.reqStartDate = reqStartDate;
        this.reqEndDate = reqEndDate;
        this.reqValidity = reqValidity;
    }
}
