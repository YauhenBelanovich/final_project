package com.gmail.yauhen2012.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhen2012.repository.UserRepository;
import com.gmail.yauhen2012.repository.model.RoleEnum;
import com.gmail.yauhen2012.repository.model.User;
import com.gmail.yauhen2012.repository.model.UserDetails;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.constant.PaginationConstant;
import com.gmail.yauhen2012.service.exception.UserExistsException;
import com.gmail.yauhen2012.service.model.AddUserDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.service.util.MailSendingUtil;
import com.gmail.yauhen2012.service.util.PaginationUtil;
import com.gmail.yauhen2012.service.util.PasswordGeneratorUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.gmail.yauhen2012.service.constant.MailConstant.EMAIL_HEADER;
import static com.gmail.yauhen2012.service.constant.MailConstant.SENDER_MAILBOX_ACCESS_PASSWORD;
import static com.gmail.yauhen2012.service.constant.MailConstant.SENDER_NAME;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {this.userRepository = userRepository;}

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
        List<User> itemList = userRepository.getObjectsByPage(
                PaginationUtil.findStartPosition(pageInt),
                PaginationConstant.ITEMS_BY_PAGE
        );
        itemList.sort(Comparator.comparing(User::getEmail));
        return itemList.stream()
                .map(this::convertDatabaseObjectToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void changeRole(Long id, RoleEnum role) {
        User user = userRepository.findById(id);
        user.setRole(role);
        userRepository.merge(user);
    }

    @Override
    @Transactional
    public void changePassword(Long id) {
        String password = PasswordGeneratorUtil.generateRandomPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        User user = userRepository.findById(id);
        user.setPassword(hashedPassword);
        userRepository.merge(user);

        String contentOfTheLetter = user.getUserDetails().getFirstName() + " "
                + user.getUserDetails().getLastName() + " your password: "
                + password;
        MailSendingUtil.sendFromGMail(SENDER_NAME,
                SENDER_MAILBOX_ACCESS_PASSWORD,
                user.getEmail(),
                EMAIL_HEADER,
                contentOfTheLetter);
    }

    @Override
    @Transactional
    public UserDTO findUserById(Long id) {
        User user = userRepository.findById(id);
        return convertDatabaseObjectToDTO(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id);
        userRepository.remove(user);
    }

    public UserDTO convertDatabaseObjectToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());

        if (user.getUserDetails() != null) {
            userDTO.setLastName(user.getUserDetails().getLastName());
            userDTO.setFirstName(user.getUserDetails().getFirstName());
            userDTO.setPatronymic(user.getUserDetails().getPatronymic());
        }
        return userDTO;
    }

    private User convertAddUserDTOToDatabaseUser(AddUserDTO addUserDTO) {
        User user = new User();
        user.setEmail(addUserDTO.getEmail());

        String password = PasswordGeneratorUtil.generateRandomPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        String contentOfTheLetter = addUserDTO.getFirstName() + " "
                + addUserDTO.getLastName() + " your password: "
                + password;
        MailSendingUtil.sendFromGMail(SENDER_NAME,
                SENDER_MAILBOX_ACCESS_PASSWORD,
                addUserDTO.getEmail(),
                EMAIL_HEADER,
                contentOfTheLetter);

        user.setPassword(hashedPassword);
        user.setRole(addUserDTO.getRole());
        return user;
    }

    private boolean userExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

}
