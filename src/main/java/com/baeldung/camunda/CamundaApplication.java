package com.baeldung.camunda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Configuration
public class CamundaApplication {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CamundaApplication.class);

	public static void main(String[] args) {
		log.info("START");
		SpringApplication.run(CamundaApplication.class, args);
		log.info("FINISH");
	}

}
