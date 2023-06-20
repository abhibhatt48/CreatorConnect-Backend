package com.example.creatorconnectbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CreatorconnectBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreatorconnectBackendApplication.class, args);
	}

}
