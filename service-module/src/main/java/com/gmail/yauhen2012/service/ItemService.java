package com.gmail.yauhen2012.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import com.gmail.yauhen2012.service.exception.ItemExistsException;
import com.gmail.yauhen2012.service.model.AddItemDTO;
import com.gmail.yauhen2012.service.model.ItemDTO;
import org.xml.sax.SAXException;

public interface ItemService {

    void add(AddItemDTO addItemDTO) throws ItemExistsException;

    List<ItemDTO> findAll();

    ItemDTO findById(Long id);

    Boolean deleteItemById(Long id);

    List<ItemDTO> getItemsByPageSortedByName(String page);

    Boolean copyItem(Long id);

    Boolean loadItemsFromFile(File file) throws ParserConfigurationException, ItemExistsException, SAXException, IOException;

}
