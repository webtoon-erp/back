package com.erp.webtoon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WebtoonApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebtoonApplication.class, args);
	}

}
