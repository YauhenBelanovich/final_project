package com.gmail.yauhen2012.springbootmodule.controller;

import com.gmail.yauhen2012.repository.model.OrderStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(value = "/application-integration.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderControllerITTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    @Sql({"/data.sql"})
    public void getOrdersLikeCustomerUser_returnAllItems() throws Exception {
        mvc.perform(
                get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    @Sql({"/data.sql"})
    public void getOrdersLikeSaleUser_returnAllItems() throws Exception {
        mvc.perform(
                get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    @Sql({"/data.sql"})
    public void getNotExistOrder_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/5")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("id", "5")
        ).andExpect(status().isFound()).andExpect(redirectedUrl("/?error"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    @Sql({"/data.sql"})
    public void getOrderLikeCustomer_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("id", "1")
        ).andExpect(status().isFound()).andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    @Sql({"/data.sql"})
    public void getOrderLikeSALEUser_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("orderId", "1")
        ).andExpect(status().isOk())
                .andExpect(view().name("order"));
    }

    @Test
    @WithMockUser(roles = "SALE_USER")
    @Sql({"/data.sql"})
    public void newOrderStatus_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/1/newStatus")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("status", OrderStatusEnum.IN_PROGRESS.name())
        ).andExpect(status().isFound()).andExpect(redirectedUrl("/orders?success"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    @Sql({"/data.sql"})
    public void cartSend_returnRedirect() throws Exception {
        mvc.perform(
                get("/orders/cart/send")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("orderId", "1")
        ).andExpect(status().isFound()).andExpect(redirectedUrl("/orders"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER_USER")
    @Sql({"/data.sql"})
    public void cartEditQuantity_returnRedirect() throws Exception {

        mvc.perform(
                get("/orders/cart/editQuantity")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("itemId", "1")
                        .param("orderId", "1")
                        .param("quantity", "3")
        ).andExpect(status().isFound()).andExpect(redirectedUrl("/orders/cart"));
    }

}
