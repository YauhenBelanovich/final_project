package com.gmail.yauhen2012.springbootmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackages = {
        "com.gmail.yauhen2012.repository",
        "com.gmail.yauhen2012.service",
        "com.gmail.yauhen2012.springbootmodule"})
public class SpringBootModuleApplication {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootModuleApplication.class, args);
    }

}
