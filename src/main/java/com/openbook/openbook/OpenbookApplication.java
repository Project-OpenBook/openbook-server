package com.openbook.openbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OpenbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenbookApplication.class, args);
	}

}
