package com.hotelJB.hotelJB_API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class HotelJbApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelJbApiApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(5, new SecureRandom());
	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
