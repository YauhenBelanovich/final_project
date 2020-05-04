package com.gmail.yauhen2012.springbootmodule.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.yauhen2012.service.ReviewService;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.ReviewDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.service.model.UserInformationDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final ReviewService reviewService;

    public LoginController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<ReviewDTO> reviewDTOList = reviewService.getReviewSortedByDate();
        List<UserDTO> userDTOList = reviewDTOList.stream()
                .map(ReviewDTO::getUserId)
                .collect(Collectors.toSet())
                .stream()
                .map(userService::findUserById)
                .collect(Collectors.toList());
        model.addAttribute("reviews", reviewDTOList);
        model.addAttribute("users", userDTOList);
        logger.debug("HomePage method");
        return "home_page";
    }

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

    @GetMapping("/contacts")
    public String getUserInfoById(Model model) {
        UserDTO userDTO = userService.findUserById(1L);
        UserInformationDTO userInformationDTO = userService.findUserInformationById(1L);
        model.addAttribute("user", userDTO);
        model.addAttribute("userInfo", userInformationDTO);
        logger.debug("Get userInfoById method");
        return "contacts";

    }

}
