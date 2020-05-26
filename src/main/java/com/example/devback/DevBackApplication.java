package com.example.devback;

import com.example.devback.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class DevBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevBackApplication.class, args);
	}

}
