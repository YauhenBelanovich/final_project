package com.gmail.yauhen2012.service;

import java.util.List;

import com.gmail.yauhen2012.repository.model.OrderStatusEnum;
import com.gmail.yauhen2012.service.model.AddOrderDTO;
import com.gmail.yauhen2012.service.model.OrderDTO;

public interface OrderService {

    List<OrderDTO> findAll();

    OrderDTO findById(Long id);

    void add(AddOrderDTO addOrderDTO);

    Boolean changeStatus(Long id, OrderStatusEnum status);

    OrderDTO findOrderByUserIdWithStatusNEW(Long userId);

    void addItemsToOrder(Long oderId, List<Long> itemIdsList);

    Boolean deleteItemByIdFromOrder(Long orderId, Long ItemId);

    Boolean sendCart(Long orderId);

    List<OrderDTO> getSortedOrdersByPage(String page);

}
