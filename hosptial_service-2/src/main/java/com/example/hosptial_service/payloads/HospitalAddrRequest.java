package com.example.hosptial_service.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospitalAddrRequest {
    private String id;
    private String name;
    private String addr;
}