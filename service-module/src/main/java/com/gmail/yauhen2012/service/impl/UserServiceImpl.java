package com.gmail.yauhen2012.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhen2012.repository.UserRepository;
import com.gmail.yauhen2012.repository.model.RoleEnum;
import com.gmail.yauhen2012.repository.model.User;
import com.gmail.yauhen2012.repository.model.UserContactInformation;
import com.gmail.yauhen2012.repository.model.UserDetails;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.constant.PaginationConstant;
import com.gmail.yauhen2012.service.exception.UserExistsException;
import com.gmail.yauhen2012.service.model.AddUserDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.service.model.UserInformationDTO;
import com.gmail.yauhen2012.service.util.MailUtil;
import com.gmail.yauhen2012.service.util.PaginationUtil;
import com.gmail.yauhen2012.service.util.PasswordGeneratorUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MailUtil mailUtil;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, MailUtil mailUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailUtil = mailUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDTO loadUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return convertDatabaseObjectToDTO(user);
    }

    @Override
    @Transactional
    public void add(AddUserDTO addUserDTO) throws UserExistsException {
        if (userExists(addUserDTO.getEmail())) {
            throw new UserExistsException("User with email: " + addUserDTO.getEmail() + " already exists");
        }
        User user = convertAddUserDTOToDatabaseUser(addUserDTO);
        userRepository.add(user);

        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(addUserDTO.getFirstName());
        userDetails.setLastName(addUserDTO.getLastName());
        userDetails.setPatronymic(addUserDTO.getPatronymic());

        userDetails.setUser(user);
        user.setUserDetails(userDetails);
    }

    @Override
    @Transactional
    public List<UserDTO> getUsersByPage(String page) {
        int pageInt = Integer.parseInt(page);
        List<User> itemList = userRepository.getUsersByPageSortedByEmail(
                PaginationUtil.findStartPosition(pageInt),
                PaginationConstant.ITEMS_BY_PAGE
        );
        return itemList.stream()
                .map(this::convertDatabaseObjectToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Boolean changeRole(Long id, RoleEnum role) {
        User user = userRepository.findById(id);
        if (user != null) {
            user.setRole(role);
            userRepository.merge(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean changePassword(Long id) {
        String password = PasswordGeneratorUtil.generateRandomPassword();
        String hashedPassword = passwordEncoder.encode(password);

        User user = userRepository.findById(id);
        if (user != null) {
            user.setPassword(hashedPassword);
            userRepository.merge(user);

            String contentOfTheLetter = user.getUserDetails().getFirstName() + " "
                    + user.getUserDetails().getLastName() + " your password: "
                    + password;
            mailUtil.sendEmail(user.getEmail(), contentOfTheLetter);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public UserDTO findUserById(Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            return convertDatabaseObjectToDTO(user);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id);
        userRepository.remove(user);
    }

    @Override
    @Transactional
    public UserInformationDTO findUserInformationById(Long id) {
        User user = userRepository.findById(id);
        return getUserInformationDTOFromUser(user);
    }

    @Override
    @Transactional
    public Boolean update(UserInformationDTO userInformation) {

        User user = userRepository.findById(userInformation.getUserId());
        if (user != null) {
            if (!userInformation.getNewPassword().equals("")) {
                String hashedPassword = passwordEncoder.encode(userInformation.getNewPassword());
                user.setPassword(hashedPassword);
            }

            UserDetails userDetails = new UserDetails();
            userDetails.setUserId(userInformation.getUserId());
            userDetails.setFirstName(userInformation.getFirstName());
            userDetails.setLastName(userInformation.getLastName());
            userDetails.setPatronymic(user.getUserDetails().getPatronymic());

            user.setUserDetails(userDetails);
            userDetails.setUser(user);

            UserContactInformation userContactInformation = new UserContactInformation();
            userContactInformation.setUserId(userInformation.getUserId());
            userContactInformation.setAddress(userInformation.getAddress());
            userContactInformation.setTelephone(userInformation.getTelephone());

            user.setUserContactInformation(userContactInformation);
            userContactInformation.setUser(user);

            userRepository.merge(user);
            return true;
        }
        return false;
    }

    private UserInformationDTO getUserInformationDTOFromUser(User user) {
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setUserId(user.getId());
        userInformationDTO.setFirstName(user.getUserDetails().getFirstName());
        userInformationDTO.setLastName(user.getUserDetails().getLastName());
        if (user.getUserContactInformation() != null) {
            userInformationDTO.setAddress(user.getUserContactInformation().getAddress());
            userInformationDTO.setTelephone(user.getUserContactInformation().getTelephone());
        }
        return userInformationDTO;
    }

    private UserDTO convertDatabaseObjectToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setDeleted(user.getDeleted());

            if (user.getUserDetails() != null) {
                if (user.getDeleted()) {
                    userDTO.setLastName("(deleted)" + user.getUserDetails().getLastName());
                } else {
                    userDTO.setLastName(user.getUserDetails().getLastName());
                }
                userDTO.setFirstName(user.getUserDetails().getFirstName());
                userDTO.setPatronymic(user.getUserDetails().getPatronymic());
            }
        return userDTO;
    }

    private User convertAddUserDTOToDatabaseUser(AddUserDTO addUserDTO) {
        User user = new User();
        user.setEmail(addUserDTO.getEmail());
        user.setDeleted(false);

        String password = PasswordGeneratorUtil.generateRandomPassword();
        String hashedPassword = passwordEncoder.encode(password);

        String contentOfTheLetter = addUserDTO.getFirstName() + " "
                + addUserDTO.getLastName() + " your password: "
                + password;

        mailUtil.sendEmail(addUserDTO.getEmail(), contentOfTheLetter);

        user.setPassword(hashedPassword);
        user.setRole(addUserDTO.getRole());
        return user;
    }

    private boolean userExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

}
