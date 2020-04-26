package com.gmail.yauhen2012.service;

import java.util.List;

import com.gmail.yauhen2012.service.exception.ItemExistsException;
import com.gmail.yauhen2012.service.model.AddItemDTO;
import com.gmail.yauhen2012.service.model.ItemDTO;

public interface ItemService {

    void add(AddItemDTO addItemDTO) throws ItemExistsException;

    List<ItemDTO> findAll();

    ItemDTO findById(Long id);

    void deleteItemById(Long id);

    List<ItemDTO> getItemsByPageSortedByName(String page);

}
