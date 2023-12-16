package com.alibou.security.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PatientRecord {
    @Id
    private String id;
    private String patientId;
    @Temporal(TemporalType.DATE)
    private Date dateOfVisit;
    private String recordType; //(lab report, prescription, consultation etc);
    private String reportDetails;
    private int severity;
}