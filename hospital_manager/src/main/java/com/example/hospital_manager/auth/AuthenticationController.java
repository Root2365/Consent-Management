package com.example.hospital_manager.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hospital-addr/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  @Value("${credentials.service-name}")
  private String serviceName;
  @Value("${credentials.password}")
  private String password;
  private String hospitalServer = "http://localhost:9002/consent/";
  @Autowired
  private WebClient webClient;
  Map<String, String> auth;
  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterServiceRequest request) {
    auth = new HashMap<>();
    auth.put("email", serviceName);
    auth.put("password", password);
    String ip_address = "http://localhost:";
    String port = request.getAddr();
    webClient.post().uri(ip_address + port + "/api/v1/auth/register-admin").bodyValue(auth).retrieve()
        .bodyToMono(AuthenticationResponse.class).block();
    return ResponseEntity.ok(service.register(request));
  }

  @PostMapping("/patient-register")
  public ResponseEntity<AuthenticationResponse> registerPatients(
      @RequestBody RegisterServiceRequest request) {
    auth = new HashMap<>();
    auth.put("email", serviceName);
    auth.put("password", password);
    return ResponseEntity.ok(service.register(request));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationServiceRequest request) {
    return ResponseEntity.ok(service.authenticate(request));
  }

}
