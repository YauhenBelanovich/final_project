package com.gmail.yauhen2012.springbootmodule.controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import com.gmail.yauhen2012.service.ItemService;
import com.gmail.yauhen2012.service.exception.ItemExistsException;
import com.gmail.yauhen2012.service.model.AddItemDTO;
import com.gmail.yauhen2012.service.model.ItemDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items")
public class ItemAPIController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemService itemService;

    public ItemAPIController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Object saveItem(@RequestBody @Valid AddItemDTO addItemDTO, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return getValidationMessage(bindingResult);
        }
        try {
            logger.debug("POST API save Item method");
            itemService.add(addItemDTO);
            return "Added Successfully";
        } catch (ItemExistsException e) {
            logger.error(e.getMessage());
            return "Item exist already";
        }

    }

    @GetMapping
    public List<ItemDTO> getItems() {
        logger.debug("Get API getItems method");
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ItemDTO getItem(@PathVariable Long id) {
        logger.debug("Get API getItem method");
        return itemService.findById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable Long id) {
        itemService.deleteItemById(id);
        logger.debug("DELETE API deleteItem method");
        return "Deleted Successfully";
    }

    private String getValidationMessage(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        List<String> message = new ArrayList<>();
        for (FieldError e : errors) {
            message.add("@" + e.getField().toUpperCase() + ":" + e.getDefaultMessage());
        }
        return message.toString();
    }

}
