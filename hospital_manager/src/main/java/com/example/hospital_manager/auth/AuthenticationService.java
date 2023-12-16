package com.example.hospital_manager.auth;
import com.example.hospital_manager.config.JwtService;
import com.example.hospital_manager.entity.HospitalAddr;
import com.example.hospital_manager.repo.HospitalAddrRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final HospitalAddrRepo repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterServiceRequest request) {

    var user = HospitalAddr.builder()
            .name(request.getName())
        .password(passwordEncoder.encode(request.getPassword()))
            .addr(request.getAddr())
        .role("ROLE_AUTH")
            .id(request.getId())
        .build();
    String jwtToken = jwtService.generateToken(user);;
    if(!repository.existsById(request.getId())){
      repository.save(user);

    }
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationServiceRequest request) {
    try {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getId(),
                      request.getPassword()
              )
      );
    }
    catch (BadCredentialsException e) {
      System.out.println("Invalid Details !!");
      throw new RuntimeException("Authentication failed");
    }

    var user = repository.findById(request.getId())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }
}
