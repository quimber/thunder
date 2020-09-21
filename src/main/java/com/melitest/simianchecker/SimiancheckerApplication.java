package com.melitest.simianchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class SimiancheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimiancheckerApplication.class, args);
	}

}
