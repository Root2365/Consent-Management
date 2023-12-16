package com.alibou.security.auth;

import com.alibou.security.config.JwtService;
import com.alibou.security.exceptions.ApiException;
//import com.alibou.security.repository.TokenRepository;
import com.alibou.security.repository.PatientRepository;
import com.alibou.security.entity.Role;
import com.alibou.security.entity.Patient;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final PatientRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  static String getAlphaNumericString(int n)
  {

    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";

    String sb = "ABHA";

    for (int i = 0; i < n; i++) {
      int index
              = (int)(AlphaNumericString.length()
              * Math.random());

      sb = sb + (AlphaNumericString
              .charAt(index));
    }

    return sb;
  }

  public AuthenticationResponse register(RegisterRequest request) {
    String abId = getAlphaNumericString(10);
    if(repository.existsByEmail(request.getEmail())){
      throw new ApiException(String.format("Email %s already user exist", request.getEmail()));
    }
    while(repository.existsById(abId)){
      abId = getAlphaNumericString(10);
    }
    var user = Patient.builder()
        .name(request.getName())
        .email(request.getEmail())
        .phone(request.getPhone())
        .id(abId)
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
//    saveUserToken(savedUser, jwtToken);
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
      System.out.println("Invalid Details !!");
      throw new ApiException("Invalid username or password !!");

    }

    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
//    revokeAllUserTokens(user);
//    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

//  private void saveUserToken(User user, String jwtToken) {
//    var token = Token.builder()
//        .user(user)
//        .token(jwtToken)
//        .tokenType(TokenType.BEARER)
//        .expired(false)
//        .revoked(false)
//        .build();
//    tokenRepository.save(token);
//  }

//  private void revokeAllUserTokens(User user) {
//    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//    if (validUserTokens.isEmpty())
//      return;
//    validUserTokens.forEach(token -> {
//      token.setExpired(true);
//      token.setRevoked(true);
//    });
//    tokenRepository.saveAll(validUserTokens);
//  }
}
