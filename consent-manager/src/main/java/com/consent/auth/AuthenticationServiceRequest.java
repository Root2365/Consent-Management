package com.consent.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationServiceRequest {

  private String serviceName;
  private String password;
}
