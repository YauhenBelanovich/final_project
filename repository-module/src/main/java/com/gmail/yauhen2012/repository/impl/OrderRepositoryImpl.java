package com.gmail.yauhen2012.repository.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gmail.yauhen2012.repository.OrderRepository;
import com.gmail.yauhen2012.repository.model.Order;
import com.gmail.yauhen2012.repository.model.OrderStatusEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl extends GenericDAOImpl<Long, Order> implements OrderRepository {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Order findByUserIdWithStatusNEW(Long userId, OrderStatusEnum status) {
        String queryString = "FROM " + entityClass.getSimpleName() + " AS o" +
                " WHERE o.userId=:userId AND o.status=:status";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("userId", userId);
        query.setParameter("status", status);
        try {
            return (Order) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> getOrdersByPageSortedByDate(int startPosition, int itemsByPage) {
        String query = "from " + entityClass.getName() + " a ORDER BY a.date DESC";
        Query q = entityManager.createQuery(query);
        q.setFirstResult(startPosition);
        q.setMaxResults(itemsByPage);
        return q.getResultList();
    }

}
