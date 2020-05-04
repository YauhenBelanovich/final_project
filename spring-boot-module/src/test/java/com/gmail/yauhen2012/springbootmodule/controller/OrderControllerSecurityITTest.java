package com.gmail.yauhen2012.springbootmodule.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhen2012.repository.model.OrderStatusEnum;
import com.gmail.yauhen2012.service.OrderService;
import com.gmail.yauhen2012.service.model.OrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(value = "/application-integration.properties")
public class OrderControllerSecurityITTest {

    private static final Long TEST_ID = 1L;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getOrderWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getOrderLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getOrderLikeSaleUser_returnOk() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(TEST_ID);
        orderDTO.setTotalPrice(BigDecimal.TEN);
        when(orderService.findById(TEST_ID)).thenReturn(orderDTO);
        mvc.perform(
                get("/orders/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getOrderLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void getOrdersWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void getOrdersLikeCustomer_returnOk() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(TEST_ID);
        orderDTO.setTotalPrice(BigDecimal.TEN);
        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderDTOS.add(orderDTO);
        when(orderService.getSortedOrdersByPage("1")).thenReturn(orderDTOS);
        mvc.perform(
                get("/orders")
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void getOrdersLikeSaleUser_returnOk() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(TEST_ID);
        orderDTO.setTotalPrice(BigDecimal.TEN);
        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderDTOS.add(orderDTO);
        when(orderService.getSortedOrdersByPage("1")).thenReturn(orderDTOS);
        mvc.perform(
                get("/orders")
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void getOrdersLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void orderNewStatusWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/1/newStatus")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void orderNewStatusLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/1/newStatus")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void orderNewStatusLikeSaleUser_returnOk() throws Exception {

        when(orderService.changeStatus(TEST_ID, OrderStatusEnum.IN_PROGRESS)).thenReturn(true);
        mvc.perform(
                get("/orders/1/newStatus")
                        .param("status", OrderStatusEnum.IN_PROGRESS.name())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/orders?success"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void orderNewStatusLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/1/newStatus")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void sendCartWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/cart/send")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void sendCartLikeCustomer_returnOk() throws Exception {
        when(orderService.sendCart(TEST_ID)).thenReturn(true);
        mvc.perform(
                get("/orders/cart/send")
                        .param("orderId", "1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/orders"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void sendCartLikeSaleUser_returnRedirect() throws Exception {

        mvc.perform(
                get("/orders/cart/send")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void sendCartLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/cart/send")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void deleteItemFromOrderWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/cart/item/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void deleteItemFromOrderLikeCustomer_returnOk() throws Exception {
        when(orderService.deleteItemByIdFromOrder(TEST_ID, TEST_ID)).thenReturn(true);
        mvc.perform(
                get("/orders/cart/item/delete")
                        .param("orderId", "1")
                        .param("itemId", "1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/orders/cart"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void deleteItemFromOrderLikeSaleUser_returnRedirect() throws Exception {

        mvc.perform(
                get("/orders/cart/item/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void deleteItemFromOrderLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/cart/item/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    public void editItemQuantityWithoutRole_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/cart/editQuantity")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    public void editItemQuantityLikeCustomer_returnOk() throws Exception {
        when(orderService.deleteItemByIdFromOrder(TEST_ID, TEST_ID)).thenReturn(true);
        mvc.perform(
                get("/orders/cart/editQuantity")
                        .param("orderId", "1")
                        .param("itemId", "1")
                        .param("quantity", "3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/orders/cart"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    public void editItemQuantityLikeSaleUser_returnRedirect() throws Exception {

        mvc.perform(
                get("/orders/cart/editQuantity")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void editItemQuantityLikeAdmin_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/cart/editQuantity")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }
}
