package com.gmail.yauhen2012.service;

import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhen2012.repository.UserRepository;
import com.gmail.yauhen2012.repository.model.RoleEnum;
import com.gmail.yauhen2012.repository.model.User;
import com.gmail.yauhen2012.repository.model.UserDetails;
import com.gmail.yauhen2012.service.constant.PaginationConstant;
import com.gmail.yauhen2012.service.exception.UserExistsException;
import com.gmail.yauhen2012.service.impl.UserServiceImpl;
import com.gmail.yauhen2012.service.model.AddUserDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.service.util.PaginationUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    private UserService userService;

    private static final String TEST_EMAIL = "test@test.test";
    private static final Long TEST_ID = 1L;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void saveAlreadyExistUser_returnUserExistException() {
        AddUserDTO userDTO = new AddUserDTO();
        userDTO.setEmail(TEST_EMAIL);
        userDTO.setRole(RoleEnum.ADMINISTRATOR);
        userDTO.setFirstName("firstTest");
        userDTO.setLastName("lastTest");
        userDTO.setPatronymic("patronTest");

        User user = setUser();

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(user);
        assertThatExceptionOfType(UserExistsException.class)
                .isThrownBy(() -> userService.add(userDTO));
        org.junit.jupiter.api.Assertions.assertThrows(
                UserExistsException.class,
                () -> userService.add(userDTO),
                "User with email: " + userDTO.getEmail() + " already exists"
        );
    }

    @Test
    public void findUserByEmail_returnUser() {
        User user = setUser();
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(user);
        UserDTO userDTO = userService.loadUserByEmail(TEST_EMAIL);

        Assertions.assertThat(user.getUserDetails().getLastName().equals(userDTO.getLastName()));
        verify(userRepository, times(1)).findByEmail(anyString());

        Assertions.assertThat(userDTO).isNotNull();

    }

    @Test
    public void changePassword_verifyCallMethod() {
        User user = setUser();
        when(userRepository.findById(TEST_ID)).thenReturn(user);
        userService.changePassword(TEST_ID);

        verify(userRepository, times(1)).findById(TEST_ID);
    }

    @Test
    public void changeRole_verifyCallMethod() {
        User user = setUser();
        when(userRepository.findById(TEST_ID)).thenReturn(user);
        userService.changeRole(TEST_ID, RoleEnum.SECURE_API_USER);
        verify(userRepository, times(1)).findById(TEST_ID);
    }

    @Test
    public void findUserByID_returnUser() {
        User user = setUser();
        when(userRepository.findById(TEST_ID)).thenReturn(user);
        UserDTO userDTO = userService.findUserById(TEST_ID);

        Assertions.assertThat(user.getUserDetails().getLastName().equals(userDTO.getLastName()));
        verify(userRepository, times(1)).findById(anyLong());

        Assertions.assertThat(userDTO).isNotNull();

    }

    @Test
    public void findUsersByPage_returnUsersList() {

        String page = "1";
        List<User> users = new ArrayList<>();
        for (int i = 0; i < PaginationConstant.ITEMS_BY_PAGE; i++) {
            User user = setUser();
            users.add(user);
        }
        when(userRepository.getObjectsByPage(
                PaginationUtil.findStartPosition(Integer.parseInt(page)),
                PaginationConstant.ITEMS_BY_PAGE)).thenReturn(users);
        List<UserDTO> userDTOS = userService.getUsersByPage(page);

        Assertions.assertThat(users.size()).isEqualTo(userDTOS.size());
        verify(userRepository, times(1))
                .getObjectsByPage(
                        PaginationUtil.findStartPosition(Integer.parseInt(page)),
                        PaginationConstant.ITEMS_BY_PAGE);

        Assertions.assertThat(userDTOS).isNotEmpty();
    }

    private User setUser() {
        User user = new User();
        user.setId(TEST_ID);
        user.setEmail(TEST_EMAIL);
        user.setPassword("passwordTest");
        user.setRole(RoleEnum.ADMINISTRATOR);

        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName("firstTest");
        userDetails.setLastName("lastTest");
        userDetails.setPatronymic("patronTest");
        user.setUserDetails(userDetails);
        return user;
    }

}
