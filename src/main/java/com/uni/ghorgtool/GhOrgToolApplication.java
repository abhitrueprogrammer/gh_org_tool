package com.uni.ghorgtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class GhOrgToolApplication {

	public static void main(String[] args) {
		// Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// Set env vars manually for Spring Boot
		// System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
		// System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		// System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		// System.setProperty("SPRING_DATASOURCE_DRIVER_CLASS_NAME", dotenv.get("SPRING_DATASOURCE_DRIVER_CLASS_NAME"));
		SpringApplication.run(GhOrgToolApplication.class, args);
	}
}
