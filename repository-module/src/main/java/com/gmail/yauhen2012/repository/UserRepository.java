package com.gmail.yauhen2012.repository;

import com.gmail.yauhen2012.repository.model.User;

public interface UserRepository extends GenericDAO<Long, User> {

    User findByEmail(String email);
}
