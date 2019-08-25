package com.ahu.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/*@SpringBootApplication
public class LockApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(LockApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(LockApplication.class, args);
	}
}*/
@SpringBootApplication
@EnableAutoConfiguration
public class LockApplication{
	public static void main(String[] args) {
		SpringApplication.run(LockApplication.class, args);
	}
}