package com.gmail.yauhen2012.repository.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gmail.yauhen2012.repository.ItemRepository;
import com.gmail.yauhen2012.repository.model.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepositoryImpl extends GenericDAOImpl<Long, Item> implements ItemRepository {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Item findByUniqueNumber(String uniqueNumber) {
        String queryString = "FROM " + entityClass.getSimpleName() + " AS i" +
                " WHERE i.uniqueNumber=:uniqueNumber";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("uniqueNumber", uniqueNumber);
        try {
            return (Item) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> getItemsByPageSortedByName(int startPosition, int itemsByPage) {
        String query = "from " + entityClass.getName() + " u ORDER BY u.itemName ASC";
        Query q = entityManager.createQuery(query);
        q.setFirstResult(startPosition);
        q.setMaxResults(itemsByPage);

        return q.getResultList();
    }

}
