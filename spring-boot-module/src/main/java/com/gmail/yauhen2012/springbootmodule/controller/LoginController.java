package com.gmail.yauhen2012.springbootmodule.controller;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @GetMapping("/login")
    public String login() {
        logger.debug("Login method");
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        logger.debug("Logout method");
        return "redirect:/login";
    }

    @GetMapping("/403")
    public String accessDeniedPage() {
        logger.debug("accessDenied method");
        return "access_denied_page";
    }

}
