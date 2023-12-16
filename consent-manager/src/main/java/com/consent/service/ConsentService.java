package com.consent.service;

import com.consent.entity.Consent;

import java.util.ArrayList;
import java.util.Date;

public interface ConsentService {
    String newConsent(Consent consent);
    ArrayList<Consent> allConsentsPatient(String patientId);
    ArrayList<Consent> allConsentsDoctor(Integer doctorId, String hospitalId);
    String updateConsent(String consentId, Date startDate, Date endDate, Date validity);
    String rejectConsent(String consentId);

    Consent getConsent(String consentId);
}
