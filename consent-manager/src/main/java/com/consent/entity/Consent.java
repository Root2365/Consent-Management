package com.consent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "consent")
public class Consent {
    @Id
    private String requestId;
    private Integer doctorId;
    private String patientId;
    private String requestingHospitalId;
    private String status;
    private Date dateOfRequest;
    private Date reqStartDate;
    private Date reqEndDate;
    private Date reqValidity;
    @Temporal(TemporalType.DATE)

    private Date consentStartDate;
    @Temporal(TemporalType.DATE)
    private Date consentEndDate;
    @Temporal(TemporalType.DATE)
    private Date consentValidity;
    private String record_type;
    private Integer severity;

    public Consent() {
    }

    public Consent(Integer doctorId, String patientId, String requestingHospitalId, Date reqStartDate,Date reqEndDate,Date reqValidity, String record_type, Integer severity) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.reqStartDate = reqStartDate;
        this.reqEndDate = reqEndDate;
        this.reqValidity = reqValidity;
        this.requestingHospitalId = requestingHospitalId;
        this.record_type = record_type;
        this.severity = severity;
    }

    public Consent(Integer doctorId, String patientId,String requestingHospitalId,  String status, Date reqStartDate,Date reqEndDate,Date reqValidity, String record_type, Integer severity) {
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
