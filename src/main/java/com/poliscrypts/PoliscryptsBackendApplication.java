package com.poliscrypts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Poliscrypts API", version = "1.0", description = "Contact and Entreprise Information"))
public class PoliscryptsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoliscryptsBackendApplication.class, args);
	}

}
