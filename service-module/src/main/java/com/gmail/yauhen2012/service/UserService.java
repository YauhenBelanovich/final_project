package com.gmail.yauhen2012.service;

import java.util.List;

import com.gmail.yauhen2012.repository.model.RoleEnum;
import com.gmail.yauhen2012.service.exception.UserExistsException;
import com.gmail.yauhen2012.service.model.AddUserDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.service.model.UserInformationDTO;

public interface UserService {

    UserDTO loadUserByEmail(String email);

    void add(AddUserDTO addUserDTO) throws UserExistsException;

    List<UserDTO> getUsersByPage(String page);

    void changeRole(Long id,  RoleEnum role);

    void changePassword(Long id);

    UserDTO findUserById(Long id);

    void deleteUserById(Long id);

    UserInformationDTO findUserInformationById(Long id);

    void update(UserInformationDTO userInformation);

}
