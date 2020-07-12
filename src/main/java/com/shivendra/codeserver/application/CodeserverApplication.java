package com.shivendra.codeserver.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.shivendra.codeserver"})
@EnableJpaRepositories("com.shivendra.codeserver")
@EntityScan("com.shivendra.codeserver")
public class CodeserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeserverApplication.class, args);
	}

}
