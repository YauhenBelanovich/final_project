package com.gmail.yauhen2012.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gmail.yauhen2012.repository.ItemRepository;
import com.gmail.yauhen2012.repository.OrderRepository;
import com.gmail.yauhen2012.repository.model.Item;
import com.gmail.yauhen2012.repository.model.Order;
import com.gmail.yauhen2012.repository.model.OrderDetails;
import com.gmail.yauhen2012.repository.model.OrderStatusEnum;
import com.gmail.yauhen2012.service.constant.PaginationConstant;
import com.gmail.yauhen2012.service.impl.OrderServiceImpl;
import com.gmail.yauhen2012.service.model.AddOrderDTO;
import com.gmail.yauhen2012.service.model.OrderDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import com.gmail.yauhen2012.service.model.UserInformationDTO;
import com.gmail.yauhen2012.service.util.PaginationUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;

    private OrderService orderService;

    private static final Long TEST_ID = 1L;
    private static final String TEST_TEXT = "Test test test";
    private static final String TEST_NAME = "TestName";
    private static final String TEST_PAGE = "0";
    private static final int TEST_PAGE_INT = 0;
    private static final String TEST_EMAIL = "test@test.test";

    @BeforeEach
    public void setup() {
        orderService = new OrderServiceImpl(orderRepository, itemRepository, userService);
    }

    @Test
    public void saveOrder_returnCallMethod() {
        AddOrderDTO orderDTO = setAddOrderDTO();
        when(itemRepository.findById(TEST_ID)).thenReturn(setItem());
        orderService.add(orderDTO);
        verify(orderRepository, times(1)).add(any());
    }

    @Test
    public void sendCart_returnCallMethod() {
        Order order = setOrder();
        when(orderRepository.findById(TEST_ID)).thenReturn(order);
        Boolean bool = orderService.sendCart(TEST_ID);
        verify(orderRepository, times(1)).merge(any());
        Assertions.assertThat(bool).isTrue();
    }

    @Test
    public void findOrderByID_returnOrder() {
        Order order = setOrder();
        when(orderRepository.findById(TEST_ID)).thenReturn(order);
        when(userService.findUserById(TEST_ID)).thenReturn(setUser());
        when(userService.findUserInformationById(TEST_ID)).thenReturn(setUserInformationDTO());
        when(itemRepository.findById(TEST_ID)).thenReturn(setItem());

        OrderDTO orderDTO = orderService.findById(TEST_ID);
        Assertions.assertThat(orderDTO.getOrderId().equals(order.getOrderId()));
        verify(orderRepository, times(1)).findById(anyLong());

        Assertions.assertThat(orderDTO).isNotNull();

    }

    @Test
    public void deleteOrder_verifyCallMethod() {

        Order order = setOrder();
        when(orderRepository.findById(TEST_ID)).thenReturn(order);
        orderService.deleteItemByIdFromOrder(order.getOrderId(), order.getOrderDetailsList().get(0).getItemId());
        verify(orderRepository, times(1)).merge(any());
    }

    @Test
    public void findOrderByPage_returnOrderList() {

        List<Order> orders = new ArrayList<>();
        for (long i = 0; i < PaginationConstant.ITEMS_BY_PAGE; i++) {
            Order order = setOrder();
            order.setOrderId(i);
            orders.add(order);
        }
        when(orderRepository.getOrdersByPageSortedByDate(PaginationUtil.findStartPosition(TEST_PAGE_INT),
                PaginationConstant.ITEMS_BY_PAGE)).thenReturn(orders);
        when(userService.findUserById(TEST_ID)).thenReturn(setUser());
        when(userService.findUserInformationById(TEST_ID)).thenReturn(setUserInformationDTO());
        when(itemRepository.findById(TEST_ID)).thenReturn(setItem());
        List<OrderDTO> orderDTOS = orderService.getSortedOrdersByPage(TEST_PAGE);

        Assertions.assertThat(orders.size()).isEqualTo(orderDTOS.size());
        verify(orderRepository, times(1))
                .getOrdersByPageSortedByDate(PaginationUtil.findStartPosition(TEST_PAGE_INT), PaginationConstant.ITEMS_BY_PAGE);
        Assertions.assertThat(orderDTOS).isNotEmpty();
    }

    @Test
    public void findOrders_returnOrdersList() {

        List<Order> orders = new ArrayList<>();
        for (long i = 0; i < 5; i++) {
            Order order = setOrder();
            order.setOrderId(i);
            orders.add(order);
        }
        when(orderRepository.findAll()).thenReturn(orders);
        when(userService.findUserById(TEST_ID)).thenReturn(setUser());
        when(userService.findUserInformationById(TEST_ID)).thenReturn(setUserInformationDTO());
        when(itemRepository.findById(TEST_ID)).thenReturn(setItem());
        List<OrderDTO> orderDTOS = orderService.findAll();

        verify(orderRepository, times(1))
                .findAll();
        Assertions.assertThat(orderDTOS).isNotEmpty();
    }

    private Item setItem() {
        Item item = new Item();
        item.setItemId(TEST_ID);
        item.setItemName(TEST_NAME);
        item.setDescription(TEST_TEXT);
        item.setPrice(BigDecimal.TEN);
        return item;
    }

    private AddOrderDTO setAddOrderDTO() {
        AddOrderDTO addOrderDTO = new AddOrderDTO();
        addOrderDTO.setUserId(TEST_ID);
        List<Long> itemIdsList = new ArrayList<>();
        itemIdsList.add(TEST_ID);
        itemIdsList.add(TEST_ID);
        addOrderDTO.setItemsId(itemIdsList);
        return addOrderDTO;
    }

    private Order setOrder() {
        Order order = new Order();
        order.setUserId(TEST_ID);
        order.setOrderId(TEST_ID);
        order.setStatus(OrderStatusEnum.NEW);
        order.setDate(Date.from(Instant.now()));

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        orderDetailsList.add(setOrderDetails());
        order.setOrderDetailsList(orderDetailsList);
        return order;
    }

    private OrderDetails setOrderDetails() {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderDetailsId(TEST_ID);
        orderDetails.setOrderId(TEST_ID);
        orderDetails.setItemId(TEST_ID);
        return orderDetails;
    }

    private UserDTO setUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(TEST_EMAIL);
        return userDTO;
    }

    private UserInformationDTO setUserInformationDTO() {
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userInformationDTO.setTelephone("2222222");
        return userInformationDTO;
    }

}
