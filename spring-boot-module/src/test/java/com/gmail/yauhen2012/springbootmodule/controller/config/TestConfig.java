package com.gmail.yauhen2012.springbootmodule.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfig {

    @Bean
    UserDetailsService userDetailsService() {return mock(UserDetailsService.class);}

}
