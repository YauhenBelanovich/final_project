package com.gmail.yauhen2012.springbootmodule.controller;

import java.lang.invoke.MethodHandles;

import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.service.model.UserInformationDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/info/user")
public class UserInfoController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public UserInfoController(UserService userService) {this.userService = userService;}

    @GetMapping("/{id}")
    public String getUserInfoById(@PathVariable Long id, Model model) {
        UserDTO userDTO = userService.findUserById(id);
        UserInformationDTO userInformationDTO = userService.findUserInformationById(id);
        model.addAttribute("user", userDTO);
        model.addAttribute("userInfo", userInformationDTO);
        logger.debug("Get userInfoById method");
        return "user_info";
    }

}
