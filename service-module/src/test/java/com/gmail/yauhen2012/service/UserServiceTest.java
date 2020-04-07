package com.gmail.yauhen2012.service;

import com.gmail.yauhen2012.repository.UserRepository;
import com.gmail.yauhen2012.repository.model.User;
import com.gmail.yauhen2012.repository.model.UserDetails;
import com.gmail.yauhen2012.service.impl.UserServiceImpl;
import com.gmail.yauhen2012.service.model.UserDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void returnUserByEmail() {

        UserDTO userDTO = userService.loadUserByEmail(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());

        Assertions.assertThat(userDTO).isNotNull();

    }

    @Test
    public void changePassword_verifyChanging () {
        Long id = 1L;
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName("some string");
        userDetails.setLastName("some string");
        userDetails.setPatronymic("some string");
        User user = new User();
        user.setUserDetails(userDetails);
        when(userRepository.findById(id)).thenReturn(user);
        //Mockito.mock(MailSendingUtil.sendFromGMail());
        userService.changePassword(id);

        verify(userRepository, times(1)).findById(id);
    }
}
