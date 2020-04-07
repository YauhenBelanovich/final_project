package com.gmail.yauhen2012.service.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.gmail.yauhen2012.repository.model.RoleEnum;
import com.gmail.yauhen2012.service.constant.ValidationConstant;

public class AddUserDTO {

    @Size(min = 3, max = 40,
            message = "Must be between 3 and 40 characters long")
    @Pattern(regexp = ValidationConstant.NAME_PATTERN,
              message = "Must contain only latin letters")
    private String lastName;
    @Size(min = 3, max = 20,
            message = "Must be between 3 and 40 characters long")
    @Pattern(regexp = ValidationConstant.NAME_PATTERN,
            message = "Must contain only latin letters")
    private String firstName;
    @Size(min = 3, max = 40,
            message = "Must be between 3 and 40 characters long")
    @Pattern(regexp = ValidationConstant.NAME_PATTERN,
            message = "Must contain only latin letters")
    private String patronymic;
    @Size(min = 3, max = 50,
            message = "Must be between 3 and 40 characters long")
    @Pattern(regexp = ValidationConstant.EMAIL_PATTERN,
            message = "Incorrectly entered email")
    private String email;
    private String password;
    private RoleEnum role;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

}
