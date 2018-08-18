package com.capgemini.storeserver.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication(scanBasePackages= {"com.capgemini.storeserver"})
@EntityScan(basePackages="com.capgemini.storeserver.beans")
@EnableJpaRepositories(basePackages="com.capgemini.storeserver.repo")
@EnableWebMvc
public class CapstoreServerApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(CapstoreServerApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(CapstoreServerApplication.class);
	}
}
