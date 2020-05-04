package com.gmail.yauhen2012.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhen2012.repository.ItemRepository;
import com.gmail.yauhen2012.repository.model.Item;
import com.gmail.yauhen2012.service.constant.PaginationConstant;
import com.gmail.yauhen2012.service.exception.ItemExistsException;
import com.gmail.yauhen2012.service.impl.ItemServiceImpl;
import com.gmail.yauhen2012.service.model.AddItemDTO;
import com.gmail.yauhen2012.service.model.ItemDTO;
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
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    private ItemService itemService;

    private static final Long TEST_ID = 1L;
    private static final String TEST_TEXT = "Test test test";
    private static final String TEST_NAME = "TestName";
    private static final String TEST_PAGE = "0";
    private static final int TEST_PAGE_INT = 0;

    @BeforeEach
    public void setup() {
        itemService = new ItemServiceImpl(itemRepository);
    }

    @Test
    public void saveItem_returnCallMethod() throws ItemExistsException {
        AddItemDTO itemDTO = setAddItemDTO();
        itemService.add(itemDTO);
        verify(itemRepository, times(1)).add(any());
    }

    @Test
    public void findItemByID_returnItem() {
        Item item = setItem();
        when(itemRepository.findById(TEST_ID)).thenReturn(item);
        ItemDTO itemDTO = itemService.findById(TEST_ID);

        Assertions.assertThat(itemDTO.getItemName().equals(item.getItemName()));
        verify(itemRepository, times(1)).findById(anyLong());

        Assertions.assertThat(itemDTO).isNotNull();

    }

    @Test
    public void deleteItem_verifyCallMethod() {

        Item item = setItem();
        when(itemRepository.findById(TEST_ID)).thenReturn(item);
        itemService.deleteItemById(TEST_ID);
        verify(itemRepository, times(1)).remove(any());
    }

    @Test
    public void findItemsByPage_returnItemsList() {

        List<Item> items = new ArrayList<>();
        for (long i = 0; i < PaginationConstant.ITEMS_BY_PAGE; i++) {
            Item item = setItem();
            item.setItemId(i);
            items.add(item);
        }
        when(itemRepository.getItemsByPageSortedByName(PaginationUtil.findStartPosition(TEST_PAGE_INT),
                PaginationConstant.ITEMS_BY_PAGE)).thenReturn(items);
        List<ItemDTO> itemDTOS = itemService.getItemsByPageSortedByName(TEST_PAGE);

        Assertions.assertThat(items.size()).isEqualTo(itemDTOS.size());
        verify(itemRepository, times(1))
                .getItemsByPageSortedByName(PaginationUtil.findStartPosition(TEST_PAGE_INT), PaginationConstant.ITEMS_BY_PAGE);
        Assertions.assertThat(itemDTOS).isNotEmpty();
    }

    @Test
    public void findItems_returnItemsList() {

        List<Item> itemList = new ArrayList<>();
        for (long i = 0; i < 5; i++) {
            Item item = setItem();
            item.setItemId(i);
            itemList.add(item);
        }
        when(itemRepository.findAll()).thenReturn(itemList);
        List<ItemDTO> itemDTOList = itemService.findAll();

        verify(itemRepository, times(1))
                .findAll();
        Assertions.assertThat(itemDTOList).isNotEmpty();
    }

    private Item setItem() {
        Item item = new Item();
        item.setItemId(TEST_ID);
        item.setItemName(TEST_NAME);
        item.setDescription(TEST_TEXT);
        return item;
    }

    private AddItemDTO setAddItemDTO() {
        AddItemDTO itemDTO = new AddItemDTO();
        itemDTO.setItemName(TEST_NAME);
        itemDTO.setDescription(TEST_TEXT);
        itemDTO.setPrice(BigDecimal.TEN);
        itemDTO.setUniqueNumber(TEST_TEXT);
        return itemDTO;
    }

}
