package com.gmail.yauhen2012.springbootmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.gmail.yauhen2012.repository",
		"com.gmail.yauhen2012.service",
		"com.gmail.yauhen2012.springbootmodule"})
public class SpringBootModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootModuleApplication.class, args);
	}

}
