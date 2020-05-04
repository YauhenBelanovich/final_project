package com.gmail.yauhen2012.springbootmodule.controller;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;

import com.gmail.yauhen2012.service.ItemService;
import com.gmail.yauhen2012.service.exception.ItemExistsException;
import com.gmail.yauhen2012.service.model.ItemDTO;
import liquibase.util.file.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

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
        if (itemDTO != null) {
            model.addAttribute("item", itemDTO);
            logger.debug("Get ItemById method");
            return "item";
        }
        logger.error("Get itemById method! No items found!!!");
        return "redirect:/?error";
    }

    @GetMapping("/{id}/delete")
    public String deleteItemById(@PathVariable Long id) {
        if (itemService.deleteItemById(id)) {
            logger.debug("Get deleteItemById method");
            return "redirect:/items?success";
        }
        logger.error("Get deleteItemById method. No items found!!!");
        return "redirect:/items?error";
    }

    @GetMapping("/{id}/copy")
    public String copyItemById(@PathVariable Long id) throws ItemExistsException {
        if (itemService.copyItem(id)) {
            logger.debug("Get copyItemById method");
            return "redirect:/items?success";
        }
        logger.error("Get copyItemById method. No items found!!!");
        return "redirect:/items?error";
    }

    @PostMapping("/uploadFile")
    public String uploadXMLFile(@RequestParam("file") MultipartFile file) throws ParserConfigurationException, ItemExistsException, SAXException, IOException {

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension.equals("xml")) {
            File fileFromTempDir = multipartToFile(file);
            if (itemService.loadItemsFromFile(fileFromTempDir)) {
                logger.debug("Post uploadXML method");
                return "redirect:/items";
            } else {
                logger.debug("Post uploadXML method. Invalid file");
                return "redirect:/items?emptyFile";
            }
        }
        logger.debug("Post uploadXML method. Invalid file extension");
        return "redirect:/items?invalidExtension";
    }

    private static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + "items");
        multipart.transferTo(file);
        return file;
    }

}
