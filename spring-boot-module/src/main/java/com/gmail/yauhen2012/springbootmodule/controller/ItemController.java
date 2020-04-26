package com.gmail.yauhen2012.springbootmodule.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;

import com.gmail.yauhen2012.service.ItemService;
import com.gmail.yauhen2012.service.exception.ItemExistsException;
import com.gmail.yauhen2012.service.model.AddItemDTO;
import com.gmail.yauhen2012.service.model.ItemDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/items")
public class ItemController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemService itemService;

    public ItemController(ItemService itemService) {this.itemService = itemService;}

    @GetMapping
    public String getItemList(@RequestParam(value = "page", defaultValue = "1") String page, Model model) {
        List<ItemDTO> itemDTOList = itemService.getItemsByPageSortedByName(page);
        model.addAttribute("items", itemDTOList);
        logger.debug("Get ItemsList method");
        return "items";
    }

    @GetMapping("/{id}")
    public String getItemById(@PathVariable Long id, Model model) {
        ItemDTO itemDTO = itemService.findById(id);
        model.addAttribute("item", itemDTO);
        logger.debug("Get ItemById method");
        return "item";
    }

    @GetMapping("/{id}/delete")
    public String deleteItemById(@PathVariable Long id) {
        itemService.deleteItemById(id);
        logger.debug("Get deleteItemById method");
        return "redirect:/items";
    }

    @GetMapping("/{id}/copy")
    public String copyItemById(@PathVariable Long id) throws ItemExistsException {
        copyItem(id);
        logger.debug("Get copyItemById method");
        return "redirect:/items";
    }

    private void copyItem(Long id) throws ItemExistsException {
        ItemDTO itemDTO = itemService.findById(id);
        AddItemDTO addItemDTO = new AddItemDTO();
        addItemDTO.setItemName(itemDTO.getItemName());
        addItemDTO.setPrice(itemDTO.getPrice());
        addItemDTO.setDescription(itemDTO.getDescription());
        addItemDTO.setUniqueNumber(itemDTO.getUniqueNumber() + "(copy)");
        itemService.add(addItemDTO);
    }

}
