package com.gmail.yauhen2012.service.model;

import java.util.List;
import java.util.Objects;

import com.gmail.yauhen2012.repository.model.Article;
import com.gmail.yauhen2012.repository.model.RoleEnum;

public class UserDTO {

    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String email;
    private String password;
    private RoleEnum role;
    private List<Article> articles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) &&
                Objects.equals(lastName, userDTO.lastName) &&
                Objects.equals(firstName, userDTO.firstName) &&
                Objects.equals(patronymic, userDTO.patronymic) &&
                Objects.equals(email, userDTO.email) &&
                Objects.equals(password, userDTO.password) &&
                role == userDTO.role &&
                Objects.equals(articles, userDTO.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastName, firstName, patronymic, email, password, role, articles);
    }

}
