package com.ramiro.curso.springboot.restapp.restdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class RestdemoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(RestdemoApplication.class, args);
	}

}
