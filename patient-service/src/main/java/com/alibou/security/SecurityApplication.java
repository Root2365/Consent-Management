package com.alibou.security;

import com.alibou.security.auth.AuthenticationResponse;
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
public class SecurityApplication implements CommandLineRunner {
	@Value("${credentials.service-name}")
	private String serviceName;
	@Value("${credentials.password}")
	private String password;
	@Value("${credentials.id}")
	private String id;
	@Value("${server.port}")
	private String addr;
	private String consentServer = "http://localhost:9002/consent/";
	@Value("${hospital-manager.address}")
	private String hospitalManagerServer;
	@Autowired
	private WebClient webClient;

	Map<String,String> auth;
	Map<String,String>hospitalManagerAuth;
	@PostConstruct
	public void init() {
		auth = new HashMap<>();
		auth.put("serviceName", serviceName);
		auth.put("password", password);
		hospitalManagerAuth = new HashMap<>();
		hospitalManagerAuth.put("name",serviceName);
		hospitalManagerAuth.put("id",id);
		hospitalManagerAuth.put("password",password);
		hospitalManagerAuth.put("addr",addr);
	}
	@Override
	public void run(String... arg0) throws Exception{
		AuthenticationResponse resp = webClient.post().uri(consentServer+"api/v1/auth/register").bodyValue(auth).retrieve().bodyToMono(AuthenticationResponse.class).block();
		AuthenticationResponse resp2 = webClient.post().uri(hospitalManagerServer+"hospital-addr/api/v1/auth/patient-register").bodyValue(hospitalManagerAuth).retrieve().bodyToMono(AuthenticationResponse.class).block();

	}
	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}


}
