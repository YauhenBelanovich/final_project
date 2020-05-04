package com.gmail.yauhen2012.springbootmodule.controller;

import java.math.BigDecimal;

import com.gmail.yauhen2012.service.ItemService;
import com.gmail.yauhen2012.service.model.ItemDTO;
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

@WebMvcTest(controllers = ItemController.class, excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
@ActiveProfiles("test")
class ItemControllerTest {

    private static final Long TEST_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    @WithMockUser
    void getItemsPage_returnOk() throws Exception {
        this.mockMvc.perform(
                get("/items")
        ).andExpect(status().isOk())
                .andExpect(view()
                        .name("items"));
    }

    @Test
    @WithMockUser
    void getDeleteItem_returnRedirect() throws Exception {
        when(itemService.deleteItemById(TEST_ID)).thenReturn(true);
        this.mockMvc.perform(
                get("/items/1/delete")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/items?success"));
    }

    @Test
    @WithMockUser
    void geCopyItem_returnRedirect() throws Exception {
        when(itemService.copyItem(TEST_ID)).thenReturn(true);
        this.mockMvc.perform(
                get("/items/1/copy")
        ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/items?success"));
    }

    @Test
    @WithMockUser
    void getItem_returnOk() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(TEST_ID);
        itemDTO.setItemName("TestName");
        itemDTO.setPrice(BigDecimal.TEN);

        when(itemService.findById(TEST_ID)).thenReturn(itemDTO);
        this.mockMvc.perform(
                get("/items/1")
                        .param("id", "1")
        ).andExpect(status().isOk()).andExpect(view().name("item"));
    }
}