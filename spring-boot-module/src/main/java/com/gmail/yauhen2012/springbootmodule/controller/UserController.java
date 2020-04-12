package com.gmail.yauhen2012.springbootmodule.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.validation.Valid;

import com.gmail.yauhen2012.repository.model.RoleEnum;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.exception.UserExistsException;
import com.gmail.yauhen2012.service.model.AddUserDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.springbootmodule.constant.AdministratorConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public UserController(UserService userService) {this.userService = userService;}

    @GetMapping
    public String getUsersList(@RequestParam(value = "page", defaultValue = "1") String page, Model model) {
        List<UserDTO> userDTOList = userService.getUsersByPage(page);
        model.addAttribute("users", userDTOList);
        model.addAttribute("administratorEmail", AdministratorConstant.ADMINISTRATOR_EMAIL);
        logger.debug("Get ItemList method");
        return "users";
    }

    @GetMapping("/new")
    public String addUserPage(Model model) {
        model.addAttribute("user", new AddUserDTO());
        logger.debug("Get addUserPage method");
        return "registration";
    }

    @PostMapping("/new")
    public String addUser(@Valid @ModelAttribute(name = "user") AddUserDTO user, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("user", user);
            return "registration";
        } else {
            try {
                userService.add(user);
                logger.debug("Post addUser method");
                return "redirect:/users";
            } catch (UserExistsException e) {
                logger.error(e.getMessage());
                model.addAttribute("userAlreadyExistsError", e.getMessage());
                return "registration";
            }
        }
    }

    @PostMapping("/delete")
    public String deleteUsersById(@RequestParam(value = "ids", required = false) Long[] ids, RedirectAttributes redirectAttributes) {
        if (ids != null) {
            for (Long id : ids) {
                userService.deleteUserById(id);
            }
            logger.debug("Get deleteUsers method");
        } else {
            redirectAttributes.addFlashAttribute("noUsersSelectedMessage", "No users selected");
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/newPassword")
    public String sendNewPassword(@PathVariable Long id) {
        userService.changePassword(id);
        logger.debug("Get new password method");
        return "redirect:/users";
    }

    @GetMapping("/{id}/newRole")
    public String setNewRole(@PathVariable Long id, @RequestParam(name = "role") RoleEnum role) {
        userService.changeRole(id, role);
        logger.debug("Get new password method");
        return "redirect:/users";
    }

}
