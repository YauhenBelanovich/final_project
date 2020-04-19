package com.gmail.yauhen2012.repository.impl;

import com.gmail.yauhen2012.repository.UserDetailsRepository;
import com.gmail.yauhen2012.repository.model.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public class UserDetailsRepositoryImpl extends GenericDAOImpl<Long, UserDetails> implements UserDetailsRepository {

}
