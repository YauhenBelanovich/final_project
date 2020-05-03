package com.gmail.yauhen2012.repository;

import java.util.List;

import com.gmail.yauhen2012.repository.model.Order;
import com.gmail.yauhen2012.repository.model.OrderStatusEnum;

public interface OrderRepository extends GenericDAO<Long, Order> {

    Order findByUserIdWithStatusNEW(Long userId, OrderStatusEnum status);

    List<Order> getOrdersByPageSortedByDate(int startPosition, int itemsByPage);

}
