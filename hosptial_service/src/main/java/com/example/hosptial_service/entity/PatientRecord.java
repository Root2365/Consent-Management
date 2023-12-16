package com.example.hosptial_service.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "patientrecord")
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
