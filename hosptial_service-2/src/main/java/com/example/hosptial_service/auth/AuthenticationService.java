package com.example.hosptial_service.auth;
import com.example.hosptial_service.config.JwtService;
import com.example.hosptial_service.entity.Doctor;
import com.example.hosptial_service.entity.Role;
import com.example.hosptial_service.exceptions.ApiException;
import com.example.hosptial_service.repo.DoctorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final DoctorRepo repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  public AuthenticationResponse register(RegisterRequest request) {
    if(repository.existsByEmail(request.getEmail())){
      throw new ApiException(String.format("Email %s already user exist", request.getEmail()));
    }
    var user = Doctor.builder().name(request.getName())
            .departmant(request.getDepartmant())
            .position(request.getPosition())
            .specialization(request.getSpecialization())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.ROLE_USER)
            .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    //saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }
  public AuthenticationResponse register_admin(RegisterRequest request) {
    var user = Doctor.builder().name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.ROLE_ADMIN)
            .build();
    if(!repository.existsByEmail(request.getEmail()))
      repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    try {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                      request.getPassword()
              )
      );
    }
    catch (BadCredentialsException e) {
      System.out.println("Invalid Detials !!");
      throw new ApiException("Invalid username or password !!");

    }

    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

}
