package com.gmail.yauhen2012.service;

import com.gmail.yauhen2012.repository.model.RoleEnum;
import com.gmail.yauhen2012.service.impl.MyUserDetailsService;
import com.gmail.yauhen2012.service.model.AppUser;
import com.gmail.yauhen2012.service.model.UserDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    @Mock
    private UserService userService;
    private MyUserDetailsService myUserDetailsService;

    private static final String TEST_EMAIL = "test@test.test";
    private static final Long TEST_ID = 1L;

    @BeforeEach
    public void setup() {
        myUserDetailsService = new MyUserDetailsService(userService);
    }

    @Test
    public void tryToFindNotExistUser_returnUsernameNotFoundException() {
        UserDTO userDTO = null;

        when(userService.loadUserByEmail(TEST_EMAIL)).thenReturn(userDTO);
        Assertions.assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> myUserDetailsService.loadUserByUsername(TEST_EMAIL));
        org.junit.jupiter.api.Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> myUserDetailsService.loadUserByUsername(TEST_EMAIL),
                "User - '" + TEST_EMAIL + "' is not found"
        );
    }

    @Test
    public void returnAppUserByEmail() {
        UserDTO userDTO = setUserDTOFromDb();
        ;
        when(userService.loadUserByEmail(TEST_EMAIL)).thenReturn(userDTO);
        AppUser appUser = (AppUser) myUserDetailsService.loadUserByUsername(TEST_EMAIL);

        Assertions.assertThat(appUser).isNotNull();
        Assertions.assertThat(appUser.getUsername().equals(userDTO.getEmail()));

        verify(userService, times(1)).loadUserByEmail(TEST_EMAIL);
    }

    private UserDTO setUserDTOFromDb() {
        UserDTO user = new UserDTO();
        user.setId(TEST_ID);
        user.setEmail(TEST_EMAIL);
        user.setPassword("passwordTest");
        user.setRole(RoleEnum.ADMINISTRATOR);
        user.setFirstName("firstTest");
        user.setLastName("lastTest");
        user.setPatronymic("patronTest");
        return user;
    }

}
