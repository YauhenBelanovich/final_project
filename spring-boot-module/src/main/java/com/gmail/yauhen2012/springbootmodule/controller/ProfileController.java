package com.gmail.yauhen2012.springbootmodule.controller;

import java.lang.invoke.MethodHandles;
import javax.validation.Valid;

import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.AppUser;
import com.gmail.yauhen2012.service.model.UserInformationDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public ProfileController(UserService userService) {this.userService = userService;}

    @GetMapping
    public String getUserInformation(Model model) {

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
            Long id = getCurrentUserId();
            UserInformationDTO userInformationDTO = userService.findUserInformationById(id);

            model.addAttribute("user", userInformationDTO);
            return "profile";
        } else {
            return "redirect:/login?logout";
        }
    }

    @GetMapping("/edit")
    public String getUserInformationEditPage(Model model) {

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
            Long id = getCurrentUserId();
            UserInformationDTO userInformationDTO = userService.findUserInformationById(id);

            model.addAttribute("user", userInformationDTO);
            return "profile_edit";
        } else {
            return "redirect:/login?logout";
        }
    }

    @PostMapping("/edit")
    public String getUserInformationEdit(@Valid @ModelAttribute(name = "user") UserInformationDTO userInformation, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("user", userInformation);
            return "profile_edit";
        } else {
            userService.update(userInformation);
            logger.debug("Post addUser method");
            return "redirect:/profile";
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = (AppUser) authentication.getPrincipal();
        return userService.loadUserByEmail(user.getUsername()).getId();
    }

}
