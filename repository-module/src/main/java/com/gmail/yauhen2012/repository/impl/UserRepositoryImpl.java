package com.gmail.yauhen2012.repository.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gmail.yauhen2012.repository.UserRepository;
import com.gmail.yauhen2012.repository.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends GenericDAOImpl<Long, User> implements UserRepository {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public User findByEmail(String email) {
        String queryString = "FROM " + entityClass.getSimpleName() + " AS u" +
                " WHERE u.email=:email";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("email", email);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUsersByPageSortedByEmail(
            int startPosition,
            int itemsByPage
    ) {
        String query = "from " + entityClass.getName() + " u ORDER BY u.email ASC";
        Query q = entityManager.createQuery(query);
        q.setFirstResult(startPosition);
        q.setMaxResults(itemsByPage);

        return q.getResultList();
    }

}
