package com.example.hosptial_service;

import com.example.hosptial_service.auth.AuthenticationResponse;
import com.example.hosptial_service.auth.AuthenticationService;
import com.example.hosptial_service.auth.RegisterRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class HosptialServiceApplication implements CommandLineRunner {

	private final AuthenticationService authenticationService;

	public HosptialServiceApplication(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	@Value("${credentials.id}")
	private String id;
	@Value("${credentials.name}")
	private String name;
	@Value("${credentials.password}")
	private String password;
	@Value("${server.port}")
	private String addr;
	@Value("${hospital-manager.address}")
	private String hospitalManager;
	@Autowired
	private WebClient webClient;
	Map<String,String> auth;
	@PostConstruct
	public void init() {
		auth = new HashMap<>();
		auth.put("id", id);
		auth.put("name", name);
		auth.put("password",password);
		auth.put("addr",addr);
	}

	public static void main(String[] args) {
		SpringApplication.run(HosptialServiceApplication.class,args);
	}

	private static void startInstance( String configFile,String [] args) {
		// Create a Spring application context with the specified configuration file
		ConfigurableApplicationContext context = new SpringApplicationBuilder(HosptialServiceApplication.class)
				.properties("spring.config.location=" + configFile)
				.run();

		// Configure the embedded web server to listen on the specified port
		ConfigurableWebServerFactory serverFactory = context.getBean(ConfigurableWebServerFactory.class);
		//serverFactory.setPort(port);

		// Connect to the database using the provided configuration properties
		DataSource dataSource = context.getBean(DataSource.class);

		// Run the Spring Boot application with the embedded web server and database connection
		SpringApplication.run(HosptialServiceApplication.class,args);
	}
	@Bean
	public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
		return new TomcatServletWebServerFactory();
	}

	@Override
	public void run(String... arg0) throws Exception{
		RegisterRequest request = new  RegisterRequest("Admin", "Admin@Admin", "Admin");
        webClient.post().uri(hospitalManager+"/hospital-addr/api/v1/auth/register").bodyValue(auth).retrieve().bodyToMono(AuthenticationResponse.class).block();
		authenticationService.register_admin(request);
	}

}
