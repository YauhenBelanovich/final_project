package com.gmail.yauhen2012.springbootmodule.controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.exception.UserExistsException;
import com.gmail.yauhen2012.service.model.AddUserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserAPIController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public UserAPIController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String saveUser(@RequestBody @Valid AddUserDTO addUserDTO, BindingResult bindingResult) throws IOException, UserExistsException {
        if (bindingResult.hasErrors()) {
            return getValidationMessage(bindingResult);
        } else {
            try {
                logger.debug("POST API save User method");
                userService.add(addUserDTO);
                return "Added Successfully";

            } catch (UserExistsException e) {
                logger.error(e.getMessage());
                return "User exist already";
            }
        }

    }

    private String getValidationMessage(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        List<String> message = new ArrayList<>();
        for (FieldError e : errors) {
            message.add("@" + e.getField().toUpperCase() + ":" + e.getDefaultMessage());
        }
        return message.toString();
    }

}
