package com.gmail.yauhen2012.service;

import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhen2012.repository.UserRepository;
import com.gmail.yauhen2012.repository.model.RoleEnum;
import com.gmail.yauhen2012.repository.model.User;
import com.gmail.yauhen2012.repository.model.UserContactInformation;
import com.gmail.yauhen2012.repository.model.UserDetails;
import com.gmail.yauhen2012.service.constant.PaginationConstant;
import com.gmail.yauhen2012.service.exception.UserExistsException;
import com.gmail.yauhen2012.service.impl.UserServiceImpl;
import com.gmail.yauhen2012.service.model.AddUserDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.service.model.UserInformationDTO;
import com.gmail.yauhen2012.service.util.MailUtil;
import com.gmail.yauhen2012.service.util.PaginationUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
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
    @Mock
    private MailUtil mailUtil;
    @Mock
    private PasswordEncoder passwordEncoder;

    private static final String TEST_EMAIL = "test@test.test";
    private static final Long TEST_ID = 1L;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository, mailUtil, passwordEncoder);
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
        when(userRepository.getUsersByPageSortedByEmail(
                PaginationUtil.findStartPosition(Integer.parseInt(page)),
                PaginationConstant.ITEMS_BY_PAGE)).thenReturn(users);
        List<UserDTO> userDTOS = userService.getUsersByPage(page);

        Assertions.assertThat(users.size()).isEqualTo(userDTOS.size());
        verify(userRepository, times(1))
                .getUsersByPageSortedByEmail(
                        PaginationUtil.findStartPosition(Integer.parseInt(page)),
                        PaginationConstant.ITEMS_BY_PAGE);

        Assertions.assertThat(userDTOS).isNotEmpty();
    }

    @Test
    public void findUserInformationByID_returnUserInformation() {
        User user = setUser();
        when(userRepository.findById(TEST_ID)).thenReturn(user);
        UserDTO userDTO = userService.findUserById(TEST_ID);
        UserInformationDTO userInformationDTO = userService.findUserInformationById(TEST_ID);

        Assertions.assertThat(user.getUserDetails().getLastName().equals(userDTO.getLastName()));
        Assertions.assertThat(user.getUserContactInformation().getTelephone().equals(userInformationDTO.getTelephone()));
        verify(userRepository, times(2)).findById(anyLong());

        Assertions.assertThat(userDTO).isNotNull();
        Assertions.assertThat(userInformationDTO).isNotNull();

    }

    @Test
    public void updateInformation_return() {
        User user = setUser();
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setUserId(TEST_ID);
        userInformationDTO.setAddress("moscow");
        userInformationDTO.setNewPassword("");
        when(userRepository.findById(userInformationDTO.getUserId())).thenReturn(user);
        userService.update(userInformationDTO);
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).merge(any());
        Assertions.assertThat(user.getUserContactInformation().getAddress().equals(userInformationDTO.getAddress()));
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

        UserContactInformation userContactInformation = new UserContactInformation();
        userContactInformation.setAddress("moscow");
        userContactInformation.setTelephone("222-22-22");
        user.setUserContactInformation(userContactInformation);
        return user;
    }

}
