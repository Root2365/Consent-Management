package com.example.hospital_manager.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationServiceRequest {
  private String id;
  private String password;
}
