package com.gmail.yauhen2012.service.impl;

import java.lang.invoke.MethodHandles;

import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.AppUser;
import com.gmail.yauhen2012.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserDTO userDTO = userService.loadUserByEmail(email);
        if (userDTO == null) {
            throw new UsernameNotFoundException("User - '" + email + "' is not found");
        }
        logger.debug("User - '" + email + "' was found");
        return new AppUser(userDTO);

    }

}
