package com.gmail.yauhen2012.springbootmodule.controller;

import java.math.BigDecimal;

import com.gmail.yauhen2012.repository.model.OrderStatusEnum;
import com.gmail.yauhen2012.service.OrderService;
import com.gmail.yauhen2012.service.UserService;
import com.gmail.yauhen2012.service.model.OrderDTO;
import com.gmail.yauhen2012.service.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = OrderController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
@ActiveProfiles("test")
class OrderControllerTest {

    private static final Long TEST_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void getOrdersPage_returnOk() throws Exception {
        this.mockMvc.perform(
                get("/orders")
        ).andExpect(status().isOk())
                .andExpect(view()
                        .name("orders"));
    }

    @Test
    @WithMockUser
    void getEditQuantity_returnRedirect() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(TEST_ID);
        when(userService.loadUserByEmail("test@test")).thenReturn(userDTO);
        when(orderService.deleteItemByIdFromOrder(TEST_ID, TEST_ID)).thenReturn(true);
        this.mockMvc.perform(
                get("/orders/cart/editQuantity")
                        .param("itemId", "1")
                .param("orderId", "1")
                .param("quantity", "1")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/orders/cart"));
    }

    @Test
    @WithMockUser
    void getNewStatus_returnRedirect() throws Exception {

        when(orderService.changeStatus(TEST_ID, OrderStatusEnum.IN_PROGRESS)).thenReturn(true);
        this.mockMvc.perform(
                get("/orders/1/newStatus")
                        .param("status", OrderStatusEnum.IN_PROGRESS.name())
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/orders?success"));
    }

    @Test
    @WithMockUser
    void deleteItemFromOrder_returnRedirect() throws Exception {

        when(orderService.deleteItemByIdFromOrder(TEST_ID, TEST_ID)).thenReturn(true);
        this.mockMvc.perform(
                get("/orders/cart/item/delete")
                        .param("itemId", "1")
                        .param("orderId", "1")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/orders/cart"));
    }

    @Test
    @WithMockUser
    void getSendCart_returnRedirect() throws Exception {
        when(orderService.sendCart(TEST_ID)).thenReturn(true);
        this.mockMvc.perform(
                get("/orders/cart/send")
                        .param("orderId", "1")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/orders"));
    }

    @Test
    @WithMockUser
    void getOrder_returnOk() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(TEST_ID);
        orderDTO.setTotalPrice(BigDecimal.TEN);

        when(orderService.findById(TEST_ID)).thenReturn(orderDTO);
        this.mockMvc.perform(
                get("/orders/1")
                        .param("id", "1")
        ).andExpect(status().isOk()).andExpect(view().name("order"));
    }
}