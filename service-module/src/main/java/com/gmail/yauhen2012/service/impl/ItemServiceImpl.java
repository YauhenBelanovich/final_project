package com.gmail.yauhen2012.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhen2012.repository.ItemRepository;
import com.gmail.yauhen2012.repository.model.Item;
import com.gmail.yauhen2012.service.ItemService;
import com.gmail.yauhen2012.service.constant.PaginationConstant;
import com.gmail.yauhen2012.service.exception.ItemExistsException;
import com.gmail.yauhen2012.service.model.AddItemDTO;
import com.gmail.yauhen2012.service.model.ItemDTO;
import com.gmail.yauhen2012.service.util.PaginationUtil;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {this.itemRepository = itemRepository;}

    @Override
    @Transactional
    public void add(AddItemDTO addItemDTO) throws ItemExistsException {
        if (itemExists(addItemDTO.getUniqueNumber())) {
            throw new ItemExistsException("Item with unique number: \"" + addItemDTO.getUniqueNumber() + "\" already exist");
        }
        Item item = convertAddItemDTOToDatabaseItem(addItemDTO);
        itemRepository.add(item);
    }

    @Override
    @Transactional
    public List<ItemDTO> findAll() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(this::convertDatabaseItemToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDTO findById(Long id) {
        Item item = itemRepository.findById(id);
        return convertDatabaseItemToDTO(item);
    }

    @Override
    @Transactional
    public void deleteItemById(Long id) {
        Item item = itemRepository.findById(id);
        itemRepository.remove(item);

    }

    @Override
    @Transactional
    public List<ItemDTO> getItemsByPageSortedByName(String page) {
        int pageInt = Integer.parseInt(page);
        List<Item> itemList = itemRepository.getItemsByPageSortedByName(
                PaginationUtil.findStartPosition(pageInt),
                PaginationConstant.ITEMS_BY_PAGE
        );
        return itemList.stream()
                .map(this::convertDatabaseItemToDTO)
                .collect(Collectors.toList());
    }

    private Item convertAddItemDTOToDatabaseItem(AddItemDTO addItemDTO) {
        Item item = new Item();
        item.setItemName(addItemDTO.getItemName());
        item.setUniqueNumber(addItemDTO.getUniqueNumber());
        item.setDescription(addItemDTO.getDescription());
        item.setPrice(addItemDTO.getPrice());
        return item;
    }

    private ItemDTO convertDatabaseItemToDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(item.getItemId());
        itemDTO.setItemName(item.getItemName());
        itemDTO.setUniqueNumber(item.getUniqueNumber());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setDescription(item.getDescription());
        return itemDTO;
    }

    private boolean itemExists(String uniqueNumber) {
        Item item = itemRepository.findByUniqueNumber(uniqueNumber);
        return item != null;
    }

}
