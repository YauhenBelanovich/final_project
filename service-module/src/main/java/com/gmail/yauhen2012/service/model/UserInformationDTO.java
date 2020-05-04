package com.gmail.yauhen2012.service.model;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.gmail.yauhen2012.service.constant.ValidationConstant;

public class UserInformationDTO {

    private Long userId;
    @Pattern(regexp = ValidationConstant.ADDRESS_PATTERN,
            message = "should be latin letters and numbers only")
    private String address;
    @Pattern(regexp = ValidationConstant.NUMBERS_PATTERN,
            message = "only numbers without spaces or other characters")
    private String telephone;
    @NotEmpty(message = "Is required")
    @Pattern(regexp = ValidationConstant.LATIN_LETTERS_PATTERN,
            message = "should be latin letters only")
    private String firstName;
    @NotEmpty(message = "Is required")
    @Pattern(regexp = ValidationConstant.LATIN_LETTERS_PATTERN,
            message = "should be latin letters only")
    private String lastName;
    @Size(min = 8, max = 16, message = "Must be between 8 and 16 characters long")
    private String newPassword;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInformationDTO that = (UserInformationDTO) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(address, that.address) &&
                Objects.equals(telephone, that.telephone) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, address, telephone, firstName, lastName, newPassword);
    }

}
