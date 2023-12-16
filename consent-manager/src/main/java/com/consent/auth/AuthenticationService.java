package com.consent.auth;

import com.consent.config.JwtService;
import com.consent.entity.User;
import com.consent.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepo repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterServiceRequest request) {

    var user = User.builder()
            .serviceName(request.getServiceName())
        .password(passwordEncoder.encode(request.getPassword()))
        .role("ROLE_AUTH")
        .build();
    if(!repository.existsByServiceName(request.getServiceName())){
//      throw new RuntimeException("This service is already registered");
      repository.save(user);
    }
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationServiceRequest request) {
    try {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getServiceName(),
                      request.getPassword()
              )
      );
    }
    catch (BadCredentialsException e) {
      System.out.println("Invalid Details !!");
      throw new RuntimeException("Authentication failed");
    }

    var user = repository.findByServiceName(request.getServiceName())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }
}
