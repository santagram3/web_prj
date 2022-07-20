package com.project.web_prj;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class WebPrjApplication {

	public static void main(String[] args) {
		log.info("\n\n============================\n\n\n");
		SpringApplication.run(WebPrjApplication.class, args);
		log.info("\n\n============================\n\n\n");
	}

}
