package com.gmail.yauhen2012.repository;

import java.util.List;

import com.gmail.yauhen2012.repository.model.User;

public interface UserRepository extends GenericDAO<Long, User> {

    User findByEmail(String email);

    List<User> getUsersByPageSortedByEmail(int startPosition, int itemsByPage);
}
