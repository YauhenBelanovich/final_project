package com.gmail.yauhen2012.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhen2012.repository.ItemRepository;
import com.gmail.yauhen2012.repository.OrderRepository;
import com.gmail.yauhen2012.repository.model.Item;
import com.gmail.yauhen2012.repository.model.Order;
import com.gmail.yauhen2012.repository.model.OrderDetails;
import com.gmail.yauhen2012.repository.model.OrderStatusEnum;
import com.gmail.yauhen2012.service.OrderService;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.constant.OrderConstant;
import com.gmail.yauhen2012.service.constant.PaginationConstant;
import com.gmail.yauhen2012.service.model.AddOrderDTO;
import com.gmail.yauhen2012.service.model.OrderDTO;
import com.gmail.yauhen2012.service.model.OrderDetailsDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.service.model.UserInformationDTO;
import com.gmail.yauhen2012.service.util.PaginationUtil;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, ItemRepository itemRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public List<OrderDTO> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertDatabaseOrderToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id);
        return convertDatabaseOrderToDTO(order);
    }

    @Override
    @Transactional
    public void add(AddOrderDTO addOrderDTO) {
        Order order = convertAddOrderToDatabaseOrder(addOrderDTO);
        orderRepository.add(order);
        List<Long> itemIds = addOrderDTO.getItemsId();
        order.setTotalPrice(itemIds.stream()
                .map(itemRepository::findById)
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (Long id : itemIds) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setItemId(id);
            orderDetails.setOrderId(order.getOrderId());

            orderDetailsList.add(orderDetails);
        }
        order.setOrderDetailsList(orderDetailsList);
    }

    @Override
    @Transactional
    public void changeStatus(Long id, OrderStatusEnum status) {
        Order order = orderRepository.findById(id);
        order.setStatus(status);
        orderRepository.merge(order);
    }

    @Override
    @Transactional
    public OrderDTO findOrderByUserIdWithStatusNEW(Long userId) {
        Order order = orderRepository.findByUserIdWithStatusNEW(userId, OrderConstant.DEFAULT_ORDER_STATUS);
        if (order != null) {
            return convertDatabaseOrderToDTO(order);
        }
        return null;
    }

    @Override
    @Transactional
    public void addItemsToOrder(Long orderId, List<Long> itemIdsList) {
        Order order = orderRepository.findById(orderId);
        List<OrderDetails> orderDetailsList = order.getOrderDetailsList();
        for (Long id : itemIdsList) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setItemId(id);
            orderDetails.setOrderId(orderId);
            orderDetailsList.add(orderDetails);
        }
        order.setTotalPrice(order.getTotalPrice().add(itemIdsList.stream()
                .map(itemRepository::findById)
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)));
        order.setOrderDetailsList(orderDetailsList);
        orderRepository.merge(order);
    }

    @Override
    @Transactional
    public void deleteItemByIdFromOrder(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId);

        List<OrderDetails> orderDetailsList = order.getOrderDetailsList();
        orderDetailsList.removeIf(orderDetails -> orderDetails.getItemId().equals(itemId));
        order.setOrderDetailsList(orderDetailsList);

        order.setTotalPrice(orderDetailsList.stream()
                .map(OrderDetails::getItemId)
                .map(itemRepository::findById)
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        orderRepository.merge(order);
    }

    @Override
    @Transactional
    public void sendCart(Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.setStatus(OrderConstant.SEND_CART_ORDER_STATUS);
        orderRepository.merge(order);
    }

    @Override
    @Transactional
    public List<OrderDTO> getSortedOrdersByPage(String page) {
        int pageInt = Integer.parseInt(page);
        List<Order> orders = orderRepository.getOrdersByPageSortedByDate(
                PaginationUtil.findStartPosition(pageInt),
                PaginationConstant.ITEMS_BY_PAGE
        );
        return orders.stream()
                .map(this::convertDatabaseOrderToDTO)
                .collect(Collectors.toList());
    }

    private Order convertAddOrderToDatabaseOrder(AddOrderDTO addOrderDTO) {
        Order order = new Order();
        order.setStatus(OrderConstant.DEFAULT_ORDER_STATUS);
        order.setUserId(addOrderDTO.getUserId());
        return order;
    }

    private OrderDTO convertDatabaseOrderToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setDate(order.getDate().toString());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTotalPrice(order.getTotalPrice());

        UserDTO userDTO = userService.findUserById(order.getUserId());
        orderDTO.setUserEmail(userDTO.getEmail());

        UserInformationDTO userInformationDTO = userService.findUserInformationById(order.getUserId());
        orderDTO.setUserTel(userInformationDTO.getTelephone());

        List<OrderDetailsDTO> orderDetailsDTOList =
                convertOrderDetailsListToOrderDetailsDTOLIst(order.getOrderDetailsList());
        orderDTO.setOrderDetailsDTOList(orderDetailsDTOList);

        return orderDTO;
    }

    private List<OrderDetailsDTO> convertOrderDetailsListToOrderDetailsDTOLIst(List<OrderDetails> orderDetailsList) {
        return orderDetailsList.stream()
                .map(OrderDetails::getItemId)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(m -> new OrderDetailsDTO(m.getKey(),
                        itemRepository.findById(m.getKey()).getItemName(),
                        m.getValue()))
                .collect(Collectors.toList());
    }

}
