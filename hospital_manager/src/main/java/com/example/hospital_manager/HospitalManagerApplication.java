package com.example.hospital_manager;

import com.example.hospital_manager.auth.AuthenticationResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class HospitalManagerApplication implements CommandLineRunner {
    @Value("${credentials.service-name}")
    private String serviceName;
    @Value("${credentials.password}")
    private String password;
    private String consentServer = "http://localhost:9002/consent/";
    @Autowired
    private WebClient webClient;

    Map<String,String> auth;
    @PostConstruct
    public void init() {
        auth = new HashMap<>();
        auth.put("serviceName", serviceName);
        auth.put("password", password);
    }

    public static void main(String[] args) {
        SpringApplication.run(HospitalManagerApplication.class, args);
    }
        @Override
        public void run(String... arg0) throws Exception{
            AuthenticationResponse resp = webClient.post().uri(consentServer+"api/v1/auth/register").bodyValue(auth).retrieve().bodyToMono(AuthenticationResponse.class).block();
            String token = resp.getToken();
        }

}
