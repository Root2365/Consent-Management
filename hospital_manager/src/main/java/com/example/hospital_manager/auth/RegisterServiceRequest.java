package com.example.hospital_manager.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterServiceRequest {
  private String id;
  private String name;
  private String password;
  private String addr;
}
