package com.gmail.yauhen2012.repository;

import java.util.List;

import com.gmail.yauhen2012.repository.model.Item;

public interface ItemRepository extends GenericDAO<Long, Item> {

    Item findByUniqueNumber(String uniqueNumber);

    List<Item> getItemsByPageSortedByName(int startPosition, int itemsByPage);

}
