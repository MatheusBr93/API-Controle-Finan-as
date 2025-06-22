package com.example.financas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.example.financas",
		"com.example.financas.Security",
		"com.example.financas.controller",
		"com.example.financas.service",
		"com.example.financas.repository",
		"com.example.financas.config",
		"com.example.financas.model",
		"com.example.financas.dto"
})
public class FinancasApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancasApplication.class, args);
	}

}
