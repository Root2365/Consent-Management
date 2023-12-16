package com.example.hospital_manager.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
@Entity
@Table(name = "patientrecords")
public class PatientRecords {
    private String patientId;
    private String hospitalId;
    @Id
    @GeneratedValue
    private Integer Id;
    public PatientRecords(String patientId, String hospitalId) {
        this.patientId = patientId;
        this.hospitalId = hospitalId;
    }

    public PatientRecords() {
    }
}
